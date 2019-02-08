# The Quest for the Abodeth
You are *insert player name*, renown adventurer and archaeologist who’s here to
raid tombs and take treasures. You’ve recently received a tip from an unknown
source that deep in the dunes of south Egypt lies a fourth pyramid unknown by the
world. So as any good archaeologist, you armed yourself with a dagger and pistol
and flew to Egypt to break in and steal that artifact in the name of research. After 40
days in the desert, you have finally found the entrance to the pyramid but only to find
that your rival, *insert rival name*, is also there. The two of you then race to the final
room where the treasure lies, fighting various monsters along the way. Your rival
reached the treasure room first, but the pharaoh possesses him and turns him into
the final boss monster. You quickly take this golden opportunity to blast the boss with
guns and rockets, getting rid of your rival and the boss at the same time. After
defeating the boss and taking his treasures, the pyramid starts to crumble down. You
quickly backtrack through the pyramid with the treasure in hand, fighting stronger
monsters who seek revenge for their dead master. The pyramid crumbles to dust just
as you escape and with the treasure in your possession.

## To Do

###Pickups
- Graphics need changing
### Enemies
- When hit set speed to 0 for 0.75 seconds
- Attack player
### Player
- Pickup weapons
- Pickup ammo
- Variables: health, ammo, weapons, currentWeapon
- Store current weapon and weapons holding
- Decrease health when hit by enemy
- Swap weapon (reapply damage stat if damage powerful in use)
### UI
- Health (bar system) (simple integer property use for this)
- Ammo
- MiniMap
- All go in HUD package
### Rooms
- Doors interactable and move player to a different room ()
- CSVs need populating
### Graphics
- Menus need creating
- Pickups need making more clear what they do
- All weapons need overlaying on player
    - Default weapons: machete & pistol


---

### Bugs
- Can shoot behind you
  - Check the direction of the player and if the mouse is in front of the player before shooting 
- Spiders get caught on walls
- Holding down the left mouse button results in NullPointerException

---
