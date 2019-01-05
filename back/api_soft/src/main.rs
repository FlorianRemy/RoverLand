#![feature(proc_macro_hygiene, decl_macro)]

#[macro_use] extern crate rocket;
#[macro_use] extern crate rocket_contrib;
#[macro_use] extern crate serde_derive;


use rocket::State;
use rocket_contrib::json::{Json, JsonValue};

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
fn get_cart( id: u32 ) -> Json<ListElement> {
    let mut liste: ListElement = ListElement::new();

    let element: Element = Element::new(12, "Rover 75".to_string(), "TBE, prix ferme".to_string(), 1500);

    liste.elements.push(element);

    Json( liste )
}

#[get("/getList")]
fn get_list() -> Json<ListElement> {
    let mut liste: ListElement = ListElement::new();
    let element: Element = Element::new(10, "bonjour".to_string(), "tres bel element".to_string(), 201);
    let element1: Element = Element::new(11, "bonjour".to_string(), "tres bel element".to_string(), 201);

    liste.elements.push(element);
    liste.elements.push(element1);

    Json( liste )
}

#[get("/getCartAmount/<id>")]
fn get_cart_amount( id: u32 ) -> JsonValue {
    let montant: u32 = 40000;

    json!({
        "amount": montant
    })
}

#[post("/addToCart", format = "json", data = "<message>")]
fn add_to_cart(message: Json<RefItemCart>) {
    let transation: RefItemCart = RefItemCart {
        id_article: message.id_article,
        id_user: message.id_user,
    };

    println!("id article {}, id_user {}", transation.id_article, transation.id_user);
}

fn main() {
    println!("Hello, world!");

    rocket::ignite().mount("/", routes![get_list, get_cart, get_cart_amount, add_to_cart]).launch();

}
