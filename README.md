# Caster: a 2D Graphic Engine

## Current objectives

The first implementation of the project will allow the user to render and utilize simple models.
Player will be able to move and have some little interaction with map point of interest.

- Display a grid map
    - Each cell is assigned a tile texture
    - A cell may be a point of interest
        - The action can be called actively by pressing a key when near it
        - The action can be called passively when standing upon the cell
        - Along with the type of action, a function to call can be provided
- Display a player icon
    - Player position in the map is to be provided
- Move the player over the map
    - Player stands still while map moves underneath
    - BE receives real time data describing player movements

## Future objectives

- Each cell can be either walkable or blocking
    - No blocking cell can be reachable from walkable ones
- User has control over map zoom
- Player can collect and manage items
    - User must be able to design items
    - An inventory panel displays items and action buttons
- Other entities can be displayed along the player

##### TODO
- [x] Design initial UML
- [ ] Create and serialize a world map example
- [ ] Implement rendering (JavaFX ?)

###### Java code written by Francesco Ostidich
