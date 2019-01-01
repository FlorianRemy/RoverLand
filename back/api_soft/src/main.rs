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

#[post("/addToCart", format = "json", data = "<message>")]
fn add_to_cart(message: Json<RefItemCart>) {

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

fn main() {
    println!("Hello, world!");

    rocket::ignite().mount("/", routes![get_list]).launch();

}
