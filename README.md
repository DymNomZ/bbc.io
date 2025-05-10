# BBC.IO

![BBC.IO Game](src/main/java/assets/titles/title.png)

## 👋 Welcome to BBC.IO!

Dive into **BBC.IO**, a thrilling free-for-all online multiplayer shooter that throws you straight into the action! Inspired by the addictive gameplay of diep.io, BBC.IO challenges you to customize your tank, outmaneuver opponents, and climb the leaderboard in a fast-paced arena.

This project isn't just a game; it's a showcase of Object-Oriented Programming (OOP) principles brought to life, offering a simple yet deeply engaging experience.

## 🚀 Features That Pack a Punch!

We've loaded BBC.IO with features to keep you battling and upgrading:

### Gameplay Highlights:
* 💥 **Intense Free-For-All Combat**: Every player for themselves! Battle it out in a dynamic arena.
* 🌐 **Real-time Multiplayer Mayhem**: Connect and compete with players worldwide thanks to UDP/TCP socket magic.
* 🎨 **Pimp Your Tank**: Unleash your creativity! Choose custom colors for your tank's body, barrel, and border.
* 🔧 **Evolve Your Arsenal**: Demolish foes, earn points, and upgrade your tank's Health, Speed, and Damage.
* 💀 **Glorious Death Messages**: Get a chuckle with humorous notifications when players (or you!) are eliminated.
* 🗺️ **Tactical Minimap**: Keep an eye on the bigger picture and track player movements across the battlefield.
* 🏆 **Climb the Leaderboard**: Rack up points and prove you're the top tank!

### Under the Hood (Technical Goodies):
* ⚙️ **Solid Client-Server Architecture**: Robust networking built with both TCP and UDP protocols.
* 🌳 **Smart Collision Detection**: Efficiently handles impacts using a QuadTree data structure, keeping gameplay smooth.
* ✨ **Sleek Graphics**: Enjoy JavaFX-powered visuals with clean grid backgrounds.
* 💾 **Persistent Player Data**: Your progress and stats are saved using MySQL.

## 💻 Tech Stack

BBC.IO is built with a powerful combination of technologies:

* **Java**: The core programming language driving the game.
* **JavaFX**: The framework bringing our game's visuals and interface to life.
* **MySQL**: Our trusty database for storing player data.
* **UDP/TCP Sockets**: Enabling seamless network communication for multiplayer action.

## ▶️ Getting Started & How to Play

Ready to roll out and dominate? Here’s how:

### Prerequisites (For Developers or Running Locally):

* Java 17 or higher
* JavaFX 17+
* MySQL Database (set up and running)
* Maven

### Joining the Battle:

1.  **Launch the Game**:
    * Enter your chosen player name on the title screen.
    * Click "**Play**" to jump into a game lobby.
2.  **Customize Your War Machine**:
    * In the lobby, pick your unique colors for the tank body, barrel, and border.
    * When you're looking sharp, click "**Ready**" to enter the arena!

### Controls:
* **Move**: `WASD` keys
* **Aim**: Use your mouse
* **Shoot**: `Left-click`
* **Toggle Death Log**: Press `T` to show/hide recent eliminations.

### Upgrade Your Tank:

* Eliminate other players to earn points.
* Spend your points to boost your tank's **Health**, **Speed**, or **Damage**. Choose wisely!

### Your Mission:
* **Survive!** Stay alive as long as you can.
* **Dominate!** Eliminate other players to rack up points.
* **Conquer!** Achieve the highest score and become the undisputed champion of the arena.

## 📂 For Developers: Project Structure

Curious about how the code is organized?

### Client-Side (`com.example.bbc` package):
* Handles the user interface and game rendering.
* Manages communication from the client to the server.
* Contains game controllers for player input.

### Server-Side (`server` package):
* Processes all game logic.
* Manages physics, including collision detection.
* Oversees player connections and data.

## 🔮 What's Next? Future Improvements

We're always thinking of ways to make BBC.IO even better! Here are some ideas for the future:

* 🆕 **More Tank Types**: Introducing tanks with unique abilities and playstyles.
* ⚡ **Power-Up System**: Collect temporary boosts and advantages in the arena.
* 🤝 **Team-Based Game Modes**: Battle alongside friends or new allies.
* 🎯 **Improved Matchmaking System**: To ensure fairer and more competitive games.

## 🧑‍💻 The Team Behind the Tanks

Meet the brilliant minds who brought BBC.IO to life:

* **Borgonia, John Dymier** - Backend & Quality Assurance
* **Emia, Seth Nathaniel** - Backend Engineering
* **Enario, Lance Joseph Lorenz** - Frontend Design & UI
* **Reyes, John Zillion Reyes** - Server Architecture & Backend
* **Tio, Raymond Gerard** - Frontend Development & UX

## 🙏 Acknowledgments

* Huge inspiration from the classic **diep.io**.
* Special thanks to **Mr. Jay Vince Serato** for guidance and support.
* A massive shout-out to all our **testers** whose feedback was invaluable!
