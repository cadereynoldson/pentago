# Pentago
- Play the game pentago against the computer! This game is command line interface based.
- The goal of the game is to get five of your tokens in a row (either b or w) by placing tokens and rotating squares
- The AI in this game is game tree. At each turn, the game tree is updated by expanding by two levels. 
- Input commands in the following format: b1/p bd WHERE:
  - b1 is the block to place your token in. (1-4)
  - p is the position to place the token in. (1-9)
  - b is the block to rotate. (1-4)
  - d is the direction to rotate the block in. (L/R)
