# Smart Pantry Manager

## 1. Project Overview

Smart Pantry Manager is a Java-based Android application developed to help users manage household ingredients and reduce food waste.

The application stores pantry ingredients and recommends recipes based on their availability. A recipe is suggested only when the user has all the required ingredients in sufficient quantities.

## 2. Project Objectives

* Manage household pantry ingredients.
* Support adding, viewing, updating, and deleting ingredients.
* Recommend recipes based on available ingredients.
* Display recipe ingredients and preparation instructions.
* Store information persistently.
* Provide a user-friendly interface.

## 3. Implemented Features

* **Pantry Management:** Add, view, edit, and delete ingredients.
* **Persistent Storage:** Save pantry and recipe information using SQLite.
* **Recipe Suggestions:** Display recipes that meet the required ingredient and quantity conditions.
* **Recipe Details:** Display recipe ingredients and preparation instructions.
* **Input Validation:** Validate ingredient information before saving.
* **Settings:** Save profile information and user preferences.
* **Feedback Messages:** Inform users when no recipes match their available ingredients.
* **Pantry Summary:** Display ingredient and expiry-related information.

## 4. Technologies Used

* Java
* Android Studio
* XML layouts
* SQLite
* RecyclerView
* Git and GitHub

## 5. Database Choice

SQLite was selected because it is integrated into Android and supports local data storage without requiring a separate database server.

It allows pantry ingredients and recipe information to persist after the application is closed. SQLite also supports the database operations needed to create, read, update, and delete records.

## 6. Database Structure

The application uses SQLite to store pantry ingredients and recipe information.

The database contains the following tables:

* **Ingredients:** Stores ingredient IDs, names, quantities, units, and expiry dates.
* **Recipes:** Stores recipe information, including names, descriptions, preparation times, and instructions.
* **Recipe Ingredients:** Links recipes to their required ingredients and quantities.

These tables support pantry management, persistent storage, and recipe matching.

*Note: The table descriptions should match the actual database schema implemented in `DatabaseHelper.java`.*

## 7. Recipe-Matching Logic

The application compares each recipe's required ingredients and quantities against the ingredients stored in the pantry.

A recipe is suggested only when all required ingredients are available in sufficient quantities. Recipes with missing ingredients or insufficient quantities are excluded from the suggestions.

This approach helps users identify recipes they can prepare with their current pantry supplies.

## 8. How to Run the Project

1. Install Android Studio.
2. Clone or download the project from GitHub.
3. Open the project in Android Studio.
4. Allow Gradle to synchronise the project.
5. Run the application using an Android emulator or a compatible Android device.

If Android Studio requests additional Android SDK components, install the required components before running the application.

## 9. Testing

The following functionality has been tested during development:

* Adding, editing, and deleting pantry ingredients.
* Saving and retrieving pantry information.
* Checking recipe suggestions against available ingredients.
* Excluding recipes when ingredients are missing or quantities are insufficient.
* Opening recipe details.
* Saving profile information and settings.
* Displaying feedback when no recipes match.
* Validating ingredient information before saving.

## 10. Project Repository

**GitHub Repository:** https://github.com/Nobubele09/Smart-Pantry-Manager

## 11. Author Information

**Student Name:** Nobubele Everjoy Sibanda
**Student Number:** 402313395
**Module:** Mobile App Development 700
