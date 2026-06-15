# Backend Server Planning & Database Schema

This document outlines the database structure and sample data required to support the MyCoffeeApp Android application.

## 1. Database Schema (Relational)

We recommend using a relational database like **PostgreSQL** or **MySQL**.

### Table: `categories`
Stores coffee categories (e.g., Espresso, Cappuccino).
| Field | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | UUID / INT | Primary Key | Unique identifier |
| `name` | VARCHAR(50) | NOT NULL | Category name |

### Table: `coffee_items`
Stores the main coffee product information.
| Field | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | UUID / INT | Primary Key | Unique identifier |
| `category_id` | UUID / INT | Foreign Key | Link to `categories.id` |
| `name` | VARCHAR(100) | NOT NULL | Product name |
| `description` | TEXT | NOT NULL | Short summary |
| `long_description` | TEXT | | Detailed info for details screen |
| `price` | DECIMAL(10,2) | NOT NULL | Base price |
| `image_url` | VARCHAR(255) | NOT NULL | Remote URL for the image |
| `rating` | DECIMAL(2,1) | DEFAULT 0.0 | Average rating |
| `review_count` | INT | DEFAULT 0 | Number of reviews |

### Table: `ingredients`
Stores ingredients for each coffee item.
| Field | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | UUID / INT | Primary Key | |
| `coffee_id` | UUID / INT | Foreign Key | Link to `coffee_items.id` |
| `name` | VARCHAR(50) | NOT NULL | e.g., "Milk", "Espresso" |

### Table: `users`
For authentication and profile management.
| Field | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | UUID / INT | Primary Key | |
| `full_name` | VARCHAR(100) | | |
| `email` | VARCHAR(100) | UNIQUE, NOT NULL | |
| `password_hash` | VARCHAR(255) | NOT NULL | |

---

## 2. Sample Data (25 Entries)

### Categories
1. { id: 1, name: "Espresso" }
2. { id: 2, name: "Cappuccino" }
3. { id: 3, name: "Latte" }
4. { id: 4, name: "Americano" }

### Coffee Items
| ID | Name | Category | Price | Rating | Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | Classic Espresso | Espresso | 150.00 | 4.8 | Pure and intense coffee shot. |
| 2 | Double Espresso | Espresso | 190.00 | 4.9 | Double the energy, double the taste. |
| 3 | Vanilla Cappuccino | Cappuccino | 250.00 | 4.7 | Creamy with a hint of vanilla. |
| 4 | Hazelnut Latte | Latte | 280.00 | 4.6 | Nutty and smooth latte. |
| 5 | Iced Americano | Americano | 180.00 | 4.5 | Refreshing black coffee over ice. |
| 6 | Caramel Macchiato | Latte | 300.00 | 4.8 | Sweet caramel drizzle on top. |
| 7 | Mocha Fusion | Cappuccino | 270.00 | 4.7 | Perfect blend of coffee and chocolate. |
| 8 | Flat White | Espresso | 220.00 | 4.9 | Smooth microfoam over espresso. |
| 9 | Irish Coffee | Espresso | 350.00 | 4.4 | Bold coffee with a cream layer. |
| 10 | Pumpkin Spice Latte | Latte | 320.00 | 4.5 | Seasonal favorite with spices. |
| 11 | Cortado | Espresso | 200.00 | 4.8 | Balanced espresso and warm milk. |
| 12 | Affogato | Espresso | 260.00 | 4.9 | Espresso poured over vanilla ice cream. |
| 13 | Cafe Au Lait | Latte | 240.00 | 4.3 | French style coffee with milk. |
| 14 | Red Eye | Americano | 210.00 | 4.6 | Drip coffee with an espresso shot. |
| 15 | Cinnamon Cappuccino | Cappuccino | 260.00 | 4.7 | Spiced up classic cappuccino. |
| 16 | Matcha Latte | Latte | 290.00 | 4.5 | Creamy green tea coffee blend. |
| 17 | Ristretto | Espresso | 160.00 | 4.8 | Short, concentrated espresso. |
| 18 | Vienna Coffee | Americano | 250.00 | 4.6 | Topped with whipped cream. |
| 19 | Turkish Coffee | Espresso | 180.00 | 4.7 | Strong, unfiltered fine grind. |
| 20 | Cold Brew | Americano | 220.00 | 4.8 | Steeped for 12 hours for smoothness. |
| 21 | Almond Milk Latte | Latte | 310.00 | 4.4 | Dairy-free smooth latte. |
| 22 | White Chocolate Mocha | Cappuccino | 330.00 | 4.7 | Rich white chocolate and espresso. |
| 23 | Lungo | Espresso | 170.00 | 4.5 | Longer pulled espresso shot. |
| 24 | Peppermint Latte | Latte | 300.00 | 4.6 | Minty and refreshing. |
| 25 | Dirty Chai Latte | Latte | 320.00 | 4.8 | Chai tea with a shot of espresso. |

---

## 3. Recommended API Endpoints

- `GET /api/categories`: Fetch all coffee categories.
- `GET /api/coffees`: Fetch all coffee items (optionally filter by `categoryId`).
- `GET /api/coffees/{id}`: Fetch detailed info for a specific coffee.
- `POST /api/users/register`: User signup.
- `POST /api/users/login`: User login.
- `GET /api/users/favorites`: Get user's saved items.
- `POST /api/orders`: Place a new order from the cart.
