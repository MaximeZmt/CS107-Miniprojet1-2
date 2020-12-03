
Animations :
	> Dans la classe RPGSprite du paquetage game.rpg.actor, nous avons décidé d'ajouter
	  des surcharges pour les méthodes extractSprites et createAnimations qui ne dépendent
	  plus de l'orientation. Il nous est désormais possible de créer un tableau de Sprites 
	  à une seule dimension ce qui nous a permis de ne pas fournir d'orientation pour des animations
	  telles que les FireSpell ou les touffes d'herbes coupées. Elles nous permettent aussi d'éviter
	  une duplication de code en utilisant des boucles for pour chaque animation.

	> Nous avons également rajouté une méthode extractSpritesVert qui nous permet de créer
	  des tableaux de Sprites lorsque l'image associée fournit une animation verticale.
	  cela nous épargne assi une utilisation moin intuitive de boucles for.
	  

Monstres :
	> La classe Monster est placée dans le paquetage game.areagame.actor car il s'agit
	  d'entités générales d'un AreaGame. C'est une classe abstraite.

	> Les acteurs monstres concrets de notre jeu sont le FlameSkull, le LogMonster et le DarkLord.
	  ils sont donc placés dans le paquetage game.arpg.actor et héritent tous de la classe Monster.

	> un FlameSkull se comporte comme une entité volante. Elle implémente donc une interface FlyableEntity
	  qui modifie les contraintes de déplacement.

	> Chez le LogMonster et le DarkLord, le temps d'innaction est exprimé seulement
	  dans leur méthode movementPattern qui est appelée quand les monstres sont dans un
	  état neutre (Idle). Cela permet de regrouper en une seule méthode la façon dont
	  ils se déplacent et de la séparer des autres états du monstre.

	> Chaque monstre possède une liste de vulnérabilités parmi des vulnérabilités existantes. 
	  C'est pourquoi nous avons créé le type enuméré Vulnerability.

	> Le DarkLord et le LogMonster sont tous deux caractérisés par un état. Nous avons donc établis
	  un type enuméré State représentant les différents états possibles
	  du monstre pour chacunes des deux créatures.

	> L'animation de mort et de perte de vie est commune à tout les monstres et sont donc mis
	  dans la classe parente Monster. 


Player :
	> Un ARPGPlayer est un Player donc hérite de la classe Player. Il posséde également un type
	  énuméré State qui permet de représenter ses différents états.


Inventaire :
	> Un inventaire est une liste non ordonnée d'objets que porte le joueur. 
	  Un ARPGInventory est un inventaire avec une notion de fortune, il hérite donc de Inventory.

	> Il possède également une représentation graphique dans le jeu avec laquelle le joueur peut
	  interagir avec.


Item/Collectable :
	> Nous avons décidé de créer une interface ItemDroppable afin que chaque acteur capable
	  de lâcher un objet ramassable par le joueur puisse le faire simplement en passant en parmetre
	  l'objet en question à une fonction commune (dropItem) pour toutes ces entités. Cette interface
	  est placée dans le paquetage game.areagame.actor car il s'agit d'un comportement général d'un acteur
	  d'un AreaGame.

	> La classe Collectable est également placée dans le paquetage game.areagame.actor car il s'agit 
	  d'une fonctionalité propre à des acteurs d'un AreaGame. Elle modélise un objet plaçable dans l'inventaire.
	  N'importe quel objet qui serait ramassable par le joueur hériterait donc de cette classe. 

	> Les objets Collectable tels que Coin ou CastleKey sont placés dans un paquetage spécifique propre
	  à eux, game.arpg.collectable car ils sont des objets concrets du jeu ARPG.


Projectile : 
	> Il s'agit d'entités volantes capable d'être projetées par le joueur (ou autre acteur).
	  les flèches (Arrow) et la magie (MagicWaterProjectile) sont des projectiles, ils héritent donc de cette classe.


PNJs :
	> Les Pnjs sont des AreaEntity (parfois MovableAreaEntity) capable de générer un dialogue.
	  ils peuvent donc interagir avec le joueur est aussi être interagit.


Dialogues :
	> Tous les dialogues sont gérés par un ARPGDialogHandler qui permet de transmettre à l'écran dans l'ordre
	  d'apparition le texte fourni par des PNJs ou autre entités capable de générer des dialolgues.
	  le texte est envoyé au joueur via une methode talkTo qui elle même renvoie le texte au handler qui se charge
	  de l'afficher correctement.

