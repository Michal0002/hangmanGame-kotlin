# Hangman Game

The application aims to allow users to play rounds of Hangman on their Android devices. The app should have a login and registration form, as well as a coin system that enables users to earn and use coins for purchasing hints. Coins will be earned based on correctly guessed words.

The application will enable users to choose the difficulty level, allowing each player to adjust the game to their skill level. Additionally, logged-in users will have the ability to add new words to the database. This way, the application will be continuously developed and expanded with new vocabulary.


## Functional requirements of the project:
* The application should have a user-friendly interface.
* The application should work on Android mobile devices.
* Users can create an account by providing their email address, username, and password.
* Users can log in using their email address and password.
* Logged-in users can add new words to the database.
* Users should be able to choose the difficulty level.
* Users should have access to hints, such as the number of letters in the word.
* Users should receive information about the remaining attempts and the game's progress.
* Users should be able to earn coins for winning games.
* Users should be able to use coins to purchase hints.
* The application should store user account information and the number of earned coins.

### Game preview
![image](https://github.com/Michal0002/hangmanGame-kotlin/assets/44274110/f30f5423-48ab-41f5-b238-44118bbb462b)
    <br/>
  Main view of the application is the login form. The user needs to log in to proceed. If they don't have an account, they must register.

![image](https://github.com/Michal0002/hangmanGame-kotlin/assets/44274110/cb907226-2b17-4e54-ab0c-33c4a6fb5b1f)
    <br/>
  Registration is done by providing the required fields such as email, password, and username. The fields cannot be empty. The username field is unique - there cannot be two users with the same username in the database.

  ![image](https://github.com/Michal0002/hangmanGame-kotlin/assets/44274110/ba176274-19b3-4e0c-9ba7-ba2a9d8f9fdc)
  The main menu displays information about the logged-in user's username and the amount of coins assigned to their account. By default, every new user receives 100 coins.

![image](https://github.com/Michal0002/hangmanGame-kotlin/assets/44274110/075536ec-23e6-4072-bd5c-2d814a72dbfa)
  <br/>
  After clicking the "Play" button, we will be redirected to the difficulty level selection. Using a dropdown list, the user can choose the appropriate difficulty level, such as easy, medium, or hard. Each difficulty level corresponds to a different number of letters in the randomly generated word.

![image](https://github.com/Michal0002/hangmanGame-kotlin/assets/44274110/1eb8a637-33c1-47ea-bc21-4f926cff2e26)
      <br/>
  After selecting the difficulty level, the user will be redirected to the gameplay. The hidden word is represented by underscores ("_"). Upon entering a correct letter that appears in the word, the underscore is replaced with the specific letter. If the letter is not present in the word, the user receives an appropriate message and the number of attempts is decreased. By default, there are 11 mistakes allowed, and after exceeding this number, the game is lost. Additionally, each incorrect answer is associated with drawing successive parts of the hangman. The user can also use hints, up to a maximum of 3 times.

![image](https://github.com/Michal0002/hangmanGame-kotlin/assets/44274110/14a5a6f9-a9b3-424e-adbf-667364cdab26)
      <br/>
  The user can also add a new word to the database. The required fields to fill in are the word's name and the choice of difficulty level.

![image](https://github.com/Michal0002/hangmanGame-kotlin/assets/44274110/4d917220-d3be-46e6-adb8-2c45f8244770)
     <br/>
  The user also has the ability to display all available words in the database.











