#![feature(proc_macro_hygiene, decl_macro)]

#[macro_use] extern crate rocket;
#[macro_use] extern crate rocket_contrib;
#[macro_use] extern crate serde_derive;

use rocket_contrib::json::{Json, JsonValue};
use std::error::Error;
use std::io;
use std::io::ErrorKind;
use std::io::prelude::*;
use std::collections::HashMap;
use std::io::{BufRead, BufReader};
use rocket::http::Status;

#[derive(Serialize, Deserialize)]
struct Element {
     id         :   u32
    ,nom        :   String
    ,description:   String
    ,prix       :   u32 
}

impl Element {
    fn new ( id: u32
            ,nom: String
            ,description: String
            ,prix: u32 ) -> Element
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

impl std::clone::Clone for Element {
    fn clone(&self) -> Element {
        Element::new(self.id, self.nom.clone(), self.description.clone(), self.prix)
    }
}

#[derive(Serialize)]
struct ListElement {
    elements: Vec<Element>
}

impl ListElement {
    fn new() -> ListElement {
        ListElement {
            elements: Vec::new(),
        }
    }
}

#[derive(Deserialize)]
struct RefItemCart {
    id_article: u32,
    id_user: u32,
}

#[get("/getCart/<id>")]
fn get_cart( id: u32 ) -> Result<Json<ListElement>, Status>{
    let mut liste: ListElement = ListElement::new();

    liste.elements = match get_cart_by_id(id) {
        Ok(l) => l,
        Err(e) => {
            return Err(Status::BadRequest);
        }
    };

    Ok( Json( liste ) )
}

#[get("/getList")]
fn get_list() -> Result<Json<ListElement>, Status> {
    let mut liste: ListElement = ListElement::new();
    liste.elements = match parse_item_from_announce_list() {
        Ok(l) => l,
        Err(e) => {
            return Err( Status::NotFound );
        }
    };

    Ok( Json( liste ) )
}

#[get("/getCartAmount/<id>")]
fn get_cart_amount( id: u32 ) -> Result<JsonValue, Status> {
    let montant: u32 = match get_cart_amount_by_id(id) {
        Ok(value) => value,
        Err(e) => {
            return Err( Status::BadRequest );
        }
    };

    Ok ( json!({
        "amount": montant
    }) )
}

#[post("/addToCart", format = "json", data = "<message>")]
fn add_to_cart(message: Json<RefItemCart>) -> Option<Status> {
    let transation: RefItemCart = RefItemCart {
        id_article: message.id_article,
        id_user: message.id_user,
    };

    match add_item_to_cart(transation) {
        Ok(_) => (),
        Err(e) => {
            return Some( Status::BadRequest );
        }
    }

    Some( Status::Accepted )
}

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

fn main() {
    rocket::ignite().mount("/", routes![get_list, get_cart, get_cart_amount, add_to_cart, delete_announcement]).launch();
}

fn append_into_cart_list(s: &str) -> std::io::Result<()> {

    let current_content = read_cart_list()?;
    let new_content = [current_content.as_str(), s].concat();

    std::fs::write("cart.txt", new_content)?;

    Ok(()) 
}

fn read_cart_list() -> Result<String, std::io::Error> {
    read_file("cart.txt")
}
 
fn read_announce_list() -> Result<String, std::io::Error> {
    read_file("list.txt")
}

fn read_file(path: &str) -> Result<String, std::io::Error> {
    std::fs::read_to_string(path)
}

fn add_item_to_cart(reference: RefItemCart) -> std::io::Result<()> {
    let article_to_add = match get_item_by_id(reference.id_article) {
        Ok(article) => article,
        Err(e) => return Err(std::io::Error::new(ErrorKind::Other, e)),
    };

    let article_string_temp: String = article_to_add.as_string();

    let line_to_add = format!("{}:{}", reference.id_user, article_string_temp);

    append_into_cart_list(line_to_add.as_str())
}

fn parse_item_from_announce_list() -> Result<Vec<Element>, std::io::Error> {
    let mut vec_temp: Vec<Element> = Vec::new();
    
    let file_as_string = read_announce_list()?;

    let splited_file = file_as_string.split('\n');

    for item in splited_file {
        if item != "" {

            let item_field: Vec<&str> = item.split(':').collect();
            
            if item_field.len() == 4 {

                for i in &item_field {
                    println!("{}", i);
                }

                let id_article: u32 = match item_field[0].parse() {
                    Ok(value) => value,
                    Err(e) => return Err(std::io::Error::new(std::io::ErrorKind::Other, e.description())),
                };

                let nom: String = item_field[1].to_string();

                let description: String = item_field[2].to_string();

                let prix: u32 = match item_field[3].parse() {
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

fn parse_item_from_cart_list() -> Result<HashMap<u32, Vec<Element>>, std::io::Error> {
    let mut map: HashMap<u32, Vec<Element>> = HashMap::new();

    let file_as_string = read_cart_list()?;

    let splited_file = file_as_string.split('\n');

    for item in splited_file {
        if item != "" {
            let item_field: Vec<&str> = item.split(':').collect();

            if item_field.len() == 5 {
                let id_user: u32 = match item_field[0].parse() {
                    Ok(value) => value,
                    Err(e) => return Err(std::io::Error::new(std::io::ErrorKind::Other, e.description())),
                };

                let id_article: u32 = match item_field[1].parse() {
                    Ok(value) => value,
                    Err(e) => return Err(std::io::Error::new(std::io::ErrorKind::Other, e.description())),
                };
                let nom: String = item_field[2].to_string();
                let description: String = item_field[3].to_string();
                let prix: u32 = match item_field[4].parse() {
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

fn get_item_by_id(id: u32) -> Result<Element, &'static str> {
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

fn get_cart_by_id(id: u32) -> Result<Vec<Element>, &'static str> {
    let map: HashMap<u32, Vec<Element>> = match parse_item_from_cart_list() {
        Ok(map) => map,
        Err(e) => return Err( "error parsing file" ),
    };

    if let Some(result) = map.get(&id) {
        Ok(result.to_vec())
    } else {
        Err("no user with this id or internal problem")
    }
}

fn get_cart_amount_by_id(id: u32) -> Result<u32, &'static str> {
    let vec = get_cart_by_id(id)?;
    let mut amount: u32 = 0;

    for elem in vec.iter() {
        amount += elem.prix;
    }

    Ok(amount)
}

fn delete_item_in_cart(reference: RefItemCart) -> Result<(), std::io::Error> {
    let file = std::fs::File::open("cart.txt")?;
    let mut string_to_write: String = String::new();

    for line in BufReader::new(file).lines() {
        match line {
            Ok(l) => {
                let item_field: Vec<&str> = l.split(':').collect();

                let id_user: u32 = match item_field[0].parse() {
                    Ok(value) => value,
                    Err(e) => return Err(std::io::Error::new(std::io::ErrorKind::Other, e.description())),
                };

                let id_article: u32 = match item_field[1].parse() {
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