#![feature(proc_macro_hygiene, decl_macro)]

#[macro_use] extern crate rocket;
#[macro_use] extern crate rocket_contrib;
#[macro_use] extern crate serde_derive;

use rocket_contrib::json::{Json, JsonValue};
use std::error::Error;
use std::io::ErrorKind;
use std::collections::HashMap;
use std::io::{BufRead, BufReader};
use rocket::http::Status;


/*** definition des structures de controle ***/

/* structure element: represente un item achetable */
#[derive(Serialize, Deserialize)]
struct Element {
     id         :   i32
    ,nom        :   String
    ,description:   String
    ,prix       :   i32 
}

/* implementation des methodes pour la structure element */
impl Element {

    fn new ( id: i32
            ,nom: String
            ,description: String
            ,prix: i32 ) -> Element
    {
        Element {
            id: id,
            nom: nom,
            description: description,
            prix: prix,
        }
    }

    fn as_string(&self) -> String {
        format!("{}:{}:{}:{}\n", self.id, self.nom, self.description, self.prix)
    }
}

/* surdefinition de la methode generique clone pour element */
impl std::clone::Clone for Element {
    fn clone(&self) -> Element {
        Element::new(self.id, self.nom.clone(), self.description.clone(), self.prix)
    }
}

/* structure ListElement: represente une liste d'elements */
#[derive(Serialize)]
struct ListElement {
    elements: Vec<Element>
}

/* implementation des methodes pour ListElement */
impl ListElement {
    fn new() -> ListElement {
        ListElement {
            elements: Vec::new(),
        }
    }
}

/* structure RefItemCart: represente la jointure entre les item disponibles et un utilisateur */
#[derive(Deserialize)]
struct RefItemCart {
    id_article: i32,
    id_user: i32,
}

/*** fin des definition des structures de controle ***/

/*** definition des handler pour les routes de l'API ***/

/* get_cart: Handler pour la route /getCart/<id>
 * renvoi la liste (listElement) des items dans le panier de l'utilisateur donne
 */
#[get("/getCart/<id>")]
fn get_cart( id: i32 ) -> Result<Json<ListElement>, Status>{
    let mut liste: ListElement = ListElement::new();

    liste.elements = match get_cart_by_id(id) {
        Ok(l) => l,
        Err(_e) => {
            return Err( Status::BadRequest );
        }
    };

    Ok( Json( liste ) )
}

/* get_list: Handler pour la route /getList
 * renvoi la liste (listElement) des annonces
 */
#[get("/getList")]
fn get_list() -> Result<Json<ListElement>, Status> {
    let mut liste: ListElement = ListElement::new();
    liste.elements = match parse_item_from_announce_list() {
        Ok(l) => l,
        Err(_e) => {
            return Err( Status::NotFound );
        }
    };

    Ok( Json( liste ) )
}

/* get_cart_amount: Handler pour la route /getCartAmount/<id>
 * renvoi un entier representant le montant du panier de l'utilisateur donne
 */
#[get("/getCartAmount/<id>")]
fn get_cart_amount( id: i32 ) -> Result<JsonValue, Status> {
    let montant: i32 = match get_cart_amount_by_id(id) {
        Ok(value) => value,
        Err(_e) => {
            return Err( Status::BadRequest );
        }
    };

    Ok ( json!({
        "amount": montant
    }) )
}

/* add_to_cart: Handler pour la route /addToCart
 * se base sur une jointure (RefItemCart) pour ajouter l'item donne au panier de l'utilisateur donne
 */
#[post("/addToCart", format = "json", data = "<message>")]
fn add_to_cart(message: Json<RefItemCart>) -> Option<Status> {
    let transation: RefItemCart = RefItemCart {
        id_article: message.id_article,
        id_user: message.id_user,
    };

    match add_item_to_cart(transation) {
        Ok(_) => (),
        Err(_e) => {
            return Some( Status::BadRequest );
        }
    }

    Some( Status::Accepted )
}

/* delete_announcement: Handler pour la route /deleteAnnouncement
 * se base sur une jointure (RefItemCart) pour supprimer toutes les instances
 * d'un item donne dans le panier d'un utilisateur donne
 **/
#[delete("/deleteAnnouncement", format = "json", data = "<message>")]
fn delete_announcement(message: Json<RefItemCart>) -> Option<Status> {
    let deletion: RefItemCart = RefItemCart {
        id_article: message.id_article,
        id_user: message.id_user,
    };

    match delete_item_in_cart(deletion) {
        Ok(_) => (),
        Err(e) => {
           return Some( Status::BadRequest );
        }
    }

    Some( Status::Accepted )
}

/*** fin des definitions des handlers pour les routes de l'API ***/

/* main: fonction principale de l'application: monte les routes de l'API */
fn main() {
    rocket::ignite().mount("/", routes![get_list, get_cart, get_cart_amount, add_to_cart, delete_announcement]).launch();
}

/* append_into_cart_list:
 * ajoute la chaine de caractere en entree au fichier de panier
 */
fn append_into_cart_list(s: &str) -> std::io::Result<()> {

    let current_content = read_cart_list()?;
    let new_content = [current_content.as_str(), s].concat();

    std::fs::write("cart.txt", new_content)?;

    Ok(()) 
}

/* read_cart_list: fonction de lecture du fichier de panier */
fn read_cart_list() -> Result<String, std::io::Error> {
    read_file("cart.txt")
}

/* read_announce_list: fonction de lecture du fichier d'annonces */
fn read_announce_list() -> Result<String, std::io::Error> {
    read_file("list.txt")
}

/* read_file: fonction de lecture d'un fichier quelconque */
fn read_file(path: &str) -> Result<String, std::io::Error> {
    std::fs::read_to_string(path)
}

/* add_item_to_cart:
 * ajoute un item donne dans un panier donne a partir de la jointure passee en entree
 */
fn add_item_to_cart(reference: RefItemCart) -> std::io::Result<()> {
    let article_to_add = match get_item_by_id(reference.id_article) {
        Ok(article) => article,
        Err(e) => return Err(std::io::Error::new(ErrorKind::Other, e)),
    };

    let article_string_temp: String = article_to_add.as_string();

    let line_to_add = format!("{}:{}", reference.id_user, article_string_temp);

    append_into_cart_list(line_to_add.as_str())
}

/* parse_item_from_announce_list:
 * lit le fichier d'annonce et le retourne sous forme d'un vecteur d'Element
 */
fn parse_item_from_announce_list() -> Result<Vec<Element>, std::io::Error> {
    let mut vec_temp: Vec<Element> = Vec::new();
    
    let file_as_string = read_announce_list()?;

    let splited_file = file_as_string.split(|c| c == '\n' || c == '\r');

    for item in splited_file {
        if item != "" {

            let item_trimmed = item.trim();
            let item_field: Vec<&str> = item_trimmed.split(':').collect();
            
            if item_field.len() == 4 {

                let id_article: i32 = match item_field[0].parse() {
                    Ok(value) => value,
                    Err(e) => return Err(std::io::Error::new(std::io::ErrorKind::Other, e.description())),
                };

                let nom: String = item_field[1].to_string();

                let description: String = item_field[2].to_string();

                let prix: i32 = match item_field[3].parse() {
                    Ok(value) => value,
                    Err(e) => return Err(std::io::Error::new(std::io::ErrorKind::Other, e.description())),
                };

                let element_temp: Element = Element::new(id_article
                                                        ,nom
                                                        ,description
                                                        ,prix
                                                        );

                vec_temp.push(element_temp);
            }
            else {
                return Err( std::io::Error::new(std::io::ErrorKind::Other, "problem with list file") );
            }
        }
    }

    Ok(vec_temp)
}

/* parse_item_from_cart_list:
 * lit le fichier de panier et renvoi une hashmap dont la cle est l'id de l'utilisateur
 * et la valeur est une liste des items contenus dans le panier
 */
fn parse_item_from_cart_list() -> Result<HashMap<i32, Vec<Element>>, std::io::Error> {
    let mut map: HashMap<i32, Vec<Element>> = HashMap::new();

    let file_as_string = read_cart_list()?;

    let splited_file = file_as_string.split(|c| c == '\n' || c == '\r');

    for item in splited_file {
        if item != "" {
            let item_trimmed = item.trim();
            let item_field: Vec<&str> = item_trimmed.split(':').collect();

            if item_field.len() == 5 {
                let id_user: i32 = match item_field[0].parse() {
                    Ok(value) => value,
                    Err(e) => return Err(std::io::Error::new(std::io::ErrorKind::Other, e.description())),
                };

                let id_article: i32 = match item_field[1].parse() {
                    Ok(value) => value,
                    Err(e) => return Err(std::io::Error::new(std::io::ErrorKind::Other, e.description())),
                };
                let nom: String = item_field[2].to_string();
                let description: String = item_field[3].to_string();
                let prix: i32 = match item_field[4].parse() {
                    Ok(value) => value,
                    Err(e) => return Err(std::io::Error::new(std::io::ErrorKind::Other, e.description())),
                };

                let element_temp: Element = Element::new(id_article
                                                        ,nom
                                                        ,description
                                                        ,prix
                                                        );

                map.entry(id_user).or_insert(Vec::new()).push(element_temp);

            }
            else {
                return Err( std::io::Error::new(std::io::ErrorKind::Other, "problem with cart file") );
            }
        }
    }

    Ok(map)
}

/* get_item_by_id:
 * recupere l'item correspondant a l'id passe en entree
 */
fn get_item_by_id(id: i32) -> Result<Element, &'static str> {
    let vec = match parse_item_from_announce_list() {
        Ok(vec) => vec,
        Err(_) => return Err( "error parsing file" ),
    };

    for elem in vec.iter() {
        
        if elem.id == id {
            return Ok(elem.clone());
        }
    }

    Err( "id not found" )
}

/* get_cart_by_id:
 * recupere la liste des items contenus dans le panier de
 * l'utilisateur dont l'id est passe en entree
 */
fn get_cart_by_id(id: i32) -> Result<Vec<Element>, &'static str> {
    let map: HashMap<i32, Vec<Element>> = match parse_item_from_cart_list() {
        Ok(map) => map,
        Err(e) => panic!( format!("error parsing file: {}", e.description()) )
    };

    if let Some(result) = map.get(&id) {
        Ok(result.to_vec())
    } else {
        Ok( Vec::new() )
    }
}

/* get_cart_amount_by_id:
 * recupere le montant (entier) du panier de l'utilisateur
 * dont l'id est passe en entree
 */
fn get_cart_amount_by_id(id: i32) -> Result<i32, &'static str> {
    let vec = get_cart_by_id(id)?;
    let mut amount: i32 = 0;

    for elem in vec.iter() {
        amount += elem.prix;
    }

    Ok(amount)
}

/* delete_item_in_cart:
 * supprime toutes les occurences d'un item donne
 * dans un panier donne a partir de la jointure passee
 * en entree
 */
fn delete_item_in_cart(reference: RefItemCart) -> Result<(), std::io::Error> {
    let file = std::fs::File::open("cart.txt")?;
    let mut string_to_write: String = String::new();

    for line in BufReader::new(file).lines() {
        match line {
            Ok(l) => {
                let item_field: Vec<&str> = l.split(':').collect();

                let id_user: i32 = match item_field[0].parse() {
                    Ok(value) => value,
                    Err(e) => return Err(std::io::Error::new(std::io::ErrorKind::Other, e.description())),
                };

                let id_article: i32 = match item_field[1].parse() {
                    Ok(value) => value,
                    Err(e) => return Err(std::io::Error::new(std::io::ErrorKind::Other, e.description())),
                };

                if (reference.id_user != id_user) || (reference.id_article != id_article) {
                    string_to_write = format!("{}{}\n", string_to_write, l);
                }
            },
            Err(e) => return Err(e),
        }
    }

    std::fs::write("cart.txt", string_to_write)?;

    Ok(())
}

