package view;


import application.GameTools;
import application.LevelData;
import application.MainGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import data.*;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

import data.GlobalGameData;

public class Drawer {
	public final int TILE_SIZE = 32;
	private Map map;
	private Vector2 lowerLeftOfView;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private boolean drawMoveCenter;
	private boolean drawBadSelection;
	private Vector2 startTouchPosition;
	private Vector2 currTouchPosition;
	private float tileScreenWidth;
	private float tileScreenHeight;
	private BitmapFont font;
	private BitmapFont bigFont;
	private CharSequence str;
	private int dispError=0;
	private float overlayFadeIn=0;
	private boolean drawGameOver =false;
	private boolean drawLevelUp=false;
	private boolean OverlayFade=false;
	private boolean[] drawHelp= {false,false,false,false};
	private BitmapFont gameOverFont;
	//private boolean initBadSelection;
	private Vector2 BadSelectionVect;
	
	public Drawer(Map map) {
		this.map = map;
		font=new BitmapFont(Gdx.files.internal("font/Dialog.fnt"), Gdx.files.internal("font/Dialog.png"), false);
		bigFont= new BitmapFont(Gdx.files.internal("font/DialogBig.fnt"), Gdx.files.internal("font/DialogBig.png"), false);
		gameOverFont = new BitmapFont(Gdx.files.internal("font/Georgia.fnt"), Gdx.files.internal("font/Georgia.png"), false);
		lowerLeftOfView = Vector2.Zero;	
		camera = new OrthographicCamera(1, GameSettings.getAspectRatio());
        camera.update();
		batch = new SpriteBatch();
		batch.enableBlending();
		str="Test Build 1.0 Width: "+GameSettings.getScreenWidth( ) +" Height: "+GameSettings.getScreenHeight( );
		Matrix4 normalProjection = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());
		batch.setProjectionMatrix(normalProjection);
	}	
	public void draw(float deltaTime) {
		setupDisplay( );
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		drawMap(deltaTime);
		drawEntities(deltaTime);
		drawVillages(deltaTime);
		drawForests(deltaTime);
		drawUi(deltaTime);	
		ResetDrawHelp();
		batch.end();
	}
	public void drawMap(float deltaTime) {		
		tileScreenWidth = GameSettings.getScreenWidth( )/TILE_SIZE;
		tileScreenHeight = GameSettings.getScreenHeight( )/TILE_SIZE;
		
		int tileOffsetX = (int)(lowerLeftOfView.x/TILE_SIZE);
		int tileOffsetY = (int)(lowerLeftOfView.y/TILE_SIZE);
		
		Vector2 viewOffset = lowerLeftOfView.cpy();
		viewOffset.sub(new Vector2(tileOffsetX*TILE_SIZE,tileOffsetY*TILE_SIZE));
		
		for (int x = 0; x < tileScreenWidth + 1; x++) {
			for (int y = 0; y < tileScreenHeight + 1; y++) {
				Vector2 position = new Vector2(x+tileOffsetX,y+tileOffsetY);
				if(!map.contains(position))
					break;
				Sprite sprite = MainGame.getTextureRepo().getTile(map.getTileType(position)).getSprite();
				drawAtLocation(sprite, x * TILE_SIZE - (int)viewOffset.x, y * TILE_SIZE - (int)viewOffset.y);
			}
		}
	}
	public void drawEntities(float deltaTime) {
		for(Entity entity : MainGame.getEntityManager( ).getEntities( )) {			
			Vector2 monsterPos = entity.getPosition().cpy().mul(TILE_SIZE).sub(lowerLeftOfView);
			if(isOnScreen(monsterPos)){
				
				//Draw HealthBars
				if(entity.getUnitType()!= EntityType.Villager)
				{
					Sprite Empty = MainGame.getTextureRepo().getUiElement(UiElement.Health).getStepInRow(0,0);
					Sprite health = MainGame.getTextureRepo().getUiElement(UiElement.Health).getStepInRow(1,0);
					health.setScale(entity.getHealthScale() , 1);
					drawAtLocation(Empty, monsterPos.x, monsterPos.y+TILE_SIZE);
					drawAtLocation(health, monsterPos.x, monsterPos.y+TILE_SIZE);
				}
				else//Assume that this entity is a villager
				{
					if(entity.getState() == AIState.Flee)
					{
						//Draw Help Bubble
						Sprite Help = MainGame.getTextureRepo().getUiElement(UiElement.HelpBubble).getStepInRow(0,0);
						//Sprite Help = MainGame.getTextureRepo().getUiElement(UiElement.Circles).getStepInRow(0,0);
						drawAtLocation(Help, monsterPos.x, monsterPos.y+TILE_SIZE);
						//System.out.println("Help me!"+monsterPos.x +" "+ monsterPos.y);
					}
				}
				drawAtLocation(entity.getSprite(), monsterPos.x, monsterPos.y);
			}
		}
	}
	public void drawVillages(float deltaTime) {
<<<<<<< HEAD
		for(Village entity : MainGame.getMap().getVillages( )) {
			Vector2 pos = entity.getPosition().cpy().mul(TILE_SIZE).sub(lowerLeftOfView);
			if(isOnScreen(pos))
				drawAtLocation(MainGame.getTextureRepo().getTile(TileType.Village).getSprite(), pos.x, pos.y);
			
=======
		for(Village entity : MainGame.getMap().getVillages( )) {	
			villageInTrouble(entity);
			Vector2 pos = entity.getPosition().cpy().mul(TILE_SIZE).sub(lowerLeftOfView);
			if(isOnScreen(pos))
				drawAtLocation(MainGame.getTextureRepo().getTile(TileType.Village).getSprite(), pos.x, pos.y);
>>>>>>> 578673f98688ed68727b7a6595659cb12112f2a5
		}
	}
	public void drawForests(float deltaTime) {
		for(Forest entity : MainGame.getMap().getForests( )) {			
			Vector2 pos = entity.getPosition().cpy().mul(TILE_SIZE).sub(lowerLeftOfView);
			if(isOnScreen(pos))
				drawAtLocation(MainGame.getTextureRepo().getTile(TileType.Forest).getSprite(), pos.x, pos.y);
			
<<<<<<< HEAD
			
		}
	}
	
	/*private boolean isOnScreen(Vector2 pos) {
=======
		}
	}
	private boolean isOnScreen(Vector2 pos) {
>>>>>>> 578673f98688ed68727b7a6595659cb12112f2a5
		Vector2 posInPixels = pos.cpy().mul(TILE_SIZE);
		Vector2 clampedLoc = posInPixels;
		
		GameTools.clamp(clampedLoc, lowerLeftOfView, getUpperRightOfView( ));
		
		return clampedLoc.epsilonEquals(posInPixels, 2);
<<<<<<< HEAD
	}	*/
	
	private boolean isOnScreen(Vector2 pos) { //This Version takes Pixel Position
        Vector2 posInPixels = pos.cpy().add(lowerLeftOfView); //.mul(TILE_SIZE);//
        Vector2 UpperRight  = getUpperRightOfView();
        
        return (posInPixels.x > lowerLeftOfView.x-(TILE_SIZE * GameSettings.getAspectRatio()) & posInPixels.y > lowerLeftOfView.y - (TILE_SIZE * GameSettings.getAspectRatio()) &
                posInPixels.x <= UpperRight.x & posInPixels.y <= UpperRight.y);
	}
	
	public boolean isOnScreenPublic(Vector2 pos){ //this Version takes Tile Position
		Vector2 temp = pos.cpy().mul(TILE_SIZE).sub(lowerLeftOfView);
		boolean value= isOnScreen(temp);
		return value;
	}
	
=======
	}	
>>>>>>> 578673f98688ed68727b7a6595659cb12112f2a5
	public void drawUi(float deltaTime) {		//STUFF I NEED TO CHANGE
		
		int MAX=99;//Max units to show in UI
		int Critical = LevelData.getCriticalNumber();//when to show the critical circle
		
		//GlobalGameData.getPlayer();
		float Scale= GameSettings.getAspectRatio();
		
		//int Villagers=MainGame.getEntityManager().getFactionMembers(Faction.Villager).size();
		int Villagers=LevelData.getVillagersTillGameOver();
		Villagers=(Villagers>MAX)? MAX : Villagers;
		float cursorSize=32;
		if(drawBadSelection)
		{
			DrawBadSelection();
		}
		else if(drawMoveCenter) {
			
			//First, get direction (one of 9 including no direction)
			Vector2 currentdirectionection=currTouchPosition.cpy();
			Vector2 cursorPos = new Vector2();//init to zero
			
			cursorPos.x=(currentdirectionection.x > (startTouchPosition.x+cursorSize))? 2: 0;
			cursorPos.x=(currentdirectionection.x < (startTouchPosition.x+cursorSize) && currentdirectionection.x > (startTouchPosition.x-cursorSize))? 1: cursorPos.x;
			
			cursorPos.y=(currentdirectionection.y > (startTouchPosition.y +cursorSize))? 0: 2;
			cursorPos.y=(currentdirectionection.y < (startTouchPosition.y +cursorSize) && currentdirectionection.y > (startTouchPosition.y-cursorSize))? 1: cursorPos.y;
			
			//Second, get appropriate cursor using math
			
			Sprite toDraw = MainGame.getTextureRepo().getUiElement(UiElement.MoveCenter).getStepInRow((int)cursorPos.x,(int)cursorPos.y);
			toDraw.setScale(Scale);
			cursorSize = toDraw.getRegionWidth() * Scale / 2;
			
			drawAtLocation(toDraw, new Vector2(startTouchPosition.x-(cursorSize), startTouchPosition.y-(cursorSize)));
			
		}
		//Now draw HUD
		//start with baseCircle
		float CircleSize=64 * Scale;
		Sprite toDraw = MainGame.getTextureRepo().getUiElement(UiElement.Circles).getStepInCol(0, 0);//get base circle
		//
		toDraw.setScale(Scale);
		//
		Vector2 DrawLoc = new Vector2(16, (camera.viewportHeight * GameSettings.getScreenHeight() - 80*Scale) );
		drawAtLocation(toDraw, DrawLoc);
		
		//Draw Empty Mana Bar (background)
		
		toDraw = MainGame.getTextureRepo().getUiElement(UiElement.ManaBar).getStepInRow(0, 0);//get Empty Mana Bar
<<<<<<< HEAD
		//
		toDraw.setScale(Scale);
		//
		drawAtLocation(toDraw, new Vector2(DrawLoc.x, DrawLoc.y));
		
		
		
		toDraw = MainGame.getTextureRepo().getUiElement(UiElement.ManaBar).getStepInRow(1, 0);//get Empty Mana Bar
		//-----------------------------
		toDraw.setScale(3*Scale,Scale);
		//-----------------------------
		drawAtLocation(toDraw, new Vector2(DrawLoc.x+(CircleSize), DrawLoc.y));
		
		toDraw = MainGame.getTextureRepo().getUiElement(UiElement.ManaBar).getStepInRow(2, 0);//get Mana Caps(little things on the end)
		//
		toDraw.setScale(Scale);
		//
=======
		drawAtLocation(toDraw, new Vector2(DrawLoc.x, DrawLoc.y));
		
		toDraw = MainGame.getTextureRepo().getUiElement(UiElement.ManaBar).getStepInRow(1, 0);//get Empty Mana Bar
		toDraw.setScale(3,1);
		drawAtLocation(toDraw, new Vector2(DrawLoc.x+(CircleSize), DrawLoc.y));
		
		toDraw = MainGame.getTextureRepo().getUiElement(UiElement.ManaBar).getStepInRow(2, 0);//get Mana Caps(little things on the end)
>>>>>>> 578673f98688ed68727b7a6595659cb12112f2a5
		drawAtLocation(toDraw, new Vector2(DrawLoc.x+( 4 * CircleSize), DrawLoc.y));
		
		//
		//	DRAW ACTUAL MANA BAR
		
<<<<<<< HEAD
		float toFill = 3 * GlobalGameData.getPlayer().getManaPercent()*Scale;//sets scaling factor for drawing filled manabar
=======
		float toFill = 3 * GlobalGameData.getPlayer().getManaPercent();//sets scaling factor for drawing filled manabar
>>>>>>> 578673f98688ed68727b7a6595659cb12112f2a5
	
		//toDraw = MainGame.getTextureRepo().getUiElement(UiElement.ManaBar).getStepInRow(0, 1);//get Filled Mana Bar part 1(static) 
		//drawAtLocation(toDraw, new Vector2(DrawLoc.x+(0 * CircleSize), DrawLoc.y));
		
		toDraw = MainGame.getTextureRepo().getUiElement(UiElement.ManaBar).getStepInRow(1, 1);//get Filled Mana Bar part 2(dynamic)
		toDraw.setScale(toFill, Scale);
		drawAtLocation(toDraw, new Vector2(DrawLoc.x+(1 * CircleSize), DrawLoc.y));
		
<<<<<<< HEAD
		float ManaWidth = 64 * toFill * Scale;
		
		//
		//  Draw Secondary Bar (Represents Villagers remaining?)
		
		toFill = 3 * LevelData.getMonstersSlainPercent()*Scale;
		//toDraw = MainGame.getTextureRepo().getUiElement(UiElement.ManaBar).getStepInRow(2, 0);//get Filled Health? Bar part 1(static) 
		//drawAtLocation(toDraw, new Vector2(DrawLoc.x+(0 * CircleSize), DrawLoc.y));
		
		toDraw = MainGame.getTextureRepo().getUiElement(UiElement.ManaBar).getStepInRow(0,1);
		//toDraw.setScale; //Set Scale Based on villagers remaining to game over
		toDraw.setScale( toFill, Scale ); //For now it will represent Monsters to slay till next level
		drawAtLocation(toDraw, new Vector2(DrawLoc.x+(1 * CircleSize), DrawLoc.y));
		
		toDraw = MainGame.getTextureRepo().getUiElement(UiElement.Circles).getStepInRow(((Villagers> Critical)? 1 : 2),0); //get OK Circle or Critical Circle
		//
		toDraw.setScale(Scale);
		//
=======
		float ManaWidth = 64 * toFill;
		
		//
		//  Draw Secondary Bar (Represents Villagers remaining?)
		
		toFill = 3 * LevelData.getMonstersSlainPercent();
		//toDraw = MainGame.getTextureRepo().getUiElement(UiElement.ManaBar).getStepInRow(2, 0);//get Filled Health? Bar part 1(static) 
		//drawAtLocation(toDraw, new Vector2(DrawLoc.x+(0 * CircleSize), DrawLoc.y));
		
		toDraw = MainGame.getTextureRepo().getUiElement(UiElement.ManaBar).getStepInRow(0,1);
		//toDraw.setScale; //Set Scale Based on villagers remaining to game over
		toDraw.setScale( toFill, 1 ); //For now it will represent Monsters to slay till next level
		drawAtLocation(toDraw, new Vector2(DrawLoc.x+(1 * CircleSize), DrawLoc.y));
		
		toDraw = MainGame.getTextureRepo().getUiElement(UiElement.Circles).getStepInRow(((Villagers> Critical)? 1 : 2),0); //get OK Circle or Critical Circle
>>>>>>> 578673f98688ed68727b7a6595659cb12112f2a5
		drawAtLocation(toDraw, DrawLoc);
		
		/*toDraw = MainGame.getTextureRepo().getUiElement(UiElement.Circles).getStepInRow(0,0);//This code originally made a background for the Monster Counter to display upon
		drawAtLocation(toDraw, DrawLoc.x, DrawLoc.y-CircleSize);
		
		toDraw = MainGame.getTextureRepo().getUiElement(UiElement.Circles).getStepInRow(2,0);
		drawAtLocation(toDraw, DrawLoc.x, DrawLoc.y-CircleSize);*/
		
		//Draw Buttons (Warrior, Archer, Mage).
		boolean canSummon=true;
<<<<<<< HEAD
		float buttonWidth;
		for(int i=0;i<4;i++)
=======
		for(int i=0;i<3;i++)
>>>>>>> 578673f98688ed68727b7a6595659cb12112f2a5
		{
			toDraw = MainGame.getTextureRepo().getUiElement(UiElement.Buttons).getStepInRow(i, 0);//get buttons
			toDraw.setScale(GameSettings.getAspectRatio());
			buttonWidth=toDraw.getRegionWidth() * Scale;
			drawAtLocation(toDraw, new Vector2((i * buttonWidth), 0));
			
			if(MainGame.getSummonHelper().getSummonCost(SummonHelper.SummonMode.values()[i+1] ) > GlobalGameData.getPlayer().getMana() )//Red Out if not enough  mana
			{
				toDraw = MainGame.getTextureRepo().getUiElement(UiElement.Buttons).getStepInRow(5, 0);
<<<<<<< HEAD
				toDraw.setScale(GameSettings.getAspectRatio());
=======
>>>>>>> 578673f98688ed68727b7a6595659cb12112f2a5
				drawAtLocation(toDraw, new Vector2((i * CircleSize), 0));
				if( MainGame.getSummonHelper().getSummonMode() == SummonHelper.SummonMode.values()[i+1] )
					canSummon=false;
					
			}
			else if(MainGame.getSummonHelper().getSummonMode() != SummonHelper.SummonMode.None)//Black out if unselected, and if one IS selected
			{
				if(MainGame.getSummonHelper().getSummonMode() != SummonHelper.SummonMode.values()[i+1] )
				{
					toDraw=MainGame.getTextureRepo().getUiElement(UiElement.Buttons).getStepInRow(4,0);
<<<<<<< HEAD
					toDraw.setScale(GameSettings.getAspectRatio());
					drawAtLocation(toDraw, new Vector2((i * buttonWidth), 0));
=======
					drawAtLocation(toDraw, new Vector2((i * CircleSize), 0));
>>>>>>> 578673f98688ed68727b7a6595659cb12112f2a5
				}
			}
			
		}
		if(canSummon)
<<<<<<< HEAD
		DrawManaCost(MainGame.getSummonHelper().getSummonMode(), DrawLoc.x + CircleSize + ManaWidth/Scale, DrawLoc.y + (CircleSize/2));
		
		//
		//Draw God Button
		/*toDraw = MainGame.getTextureRepo().getUiElement(UiElement.Buttons).getStepInRow(3, 0);
		//float godDraw = GameSettings.getScreenWidth()*Scale-toDraw.getRegionWidth()*Scale ;//this draws in lower right hand corner
		
		toDraw.setScale(Scale);
		drawAtLocation(toDraw, new Vector2(godDraw, 0));*/
		
		//Draw God Button Cooldown
		float godDraw = (3 * CircleSize);
		toDraw = MainGame.getTextureRepo().getUiElement(UiElement.Buttons).getStepInRow(6, 0);
		toDraw.setScale(Scale, GlobalGameData.getPlayer().getGodAttackPercent()*Scale);
		drawAtLocation(toDraw, new Vector2(godDraw, 0));
		
		//Draw Help Notifications (if any)
=======
		DrawManaCost(MainGame.getSummonHelper().getSummonMode(), DrawLoc.x + CircleSize + ManaWidth, DrawLoc.y + (CircleSize/2));
		
		//
		//Draw God Button
		toDraw = MainGame.getTextureRepo().getUiElement(UiElement.Buttons).getStepInRow(3, 0);
		drawAtLocation(toDraw, new Vector2((3 * CircleSize), 0));
		
		//Draw God Button Cooldown
		toDraw = MainGame.getTextureRepo().getUiElement(UiElement.Buttons).getStepInRow(6, 0);
		toDraw.setScale(1, GlobalGameData.getPlayer().getGodAttackPercent());
		drawAtLocation(toDraw, new Vector2((3 * CircleSize), 0));
		
		//Draw Help Notifications (if any)
		
>>>>>>> 578673f98688ed68727b7a6595659cb12112f2a5
		DrawHelpNotifications();
		
		float textWidth=bigFont.getBounds("00").width;
		float textHeight=bigFont.getBounds("00").height;
		
		if(drawGameOver || drawLevelUp){
			
			if(drawGameOver){drawLevelUp=false;}//lets say they happen right after another, Game Over takes priority
			
			drawOverlay((drawLevelUp)? 1: 1);
			float red  = (drawLevelUp)? (float)233/ (float)255 : 1; //if level up, its an orange color, if game over, its red
			float green= (drawLevelUp)? (float)183/ (float)255 : 0;
			float blue = (drawLevelUp)? (float)74 / (float)255 : 0;
			Color color1=new Color(red,green,blue, overlayFadeIn);
			
			//if(drawLevelUp){color=new Color(233, 183, 74, overlayFadeIn);}
			gameOverFont.setColor(color1);
<<<<<<< HEAD
			String str= (drawLevelUp)? "LEVEL "+LevelData.getLevel(): "YOU FAILED";//Holy hell do I love Dark Souls
=======
			String str= (drawLevelUp)? "LEVEL UP": "YOU FAILED";//Holy hell do I love Dark Souls
>>>>>>> 578673f98688ed68727b7a6595659cb12112f2a5
			
			Vector2 DrawCenter = new Vector2( ( (GameSettings.getScreenWidth()/2) - (gameOverFont.getBounds(str).width/2) ), ( (GameSettings.getScreenHeight()/2 ) + (gameOverFont.getBounds(str).height/2) ) );
			
			//System.out.println("Bounds: "+DrawCenter.x +" | "+DrawCenter.y);
			
			Matrix4 normalProjection = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight()); //required to render text properly
			batch.setProjectionMatrix(normalProjection);
			gameOverFont.draw(batch, str, DrawCenter.x, DrawCenter.y);
		}
		
		//ONLY DRAW TEXT AFTER THIS:
		bigFont.setColor(Color.WHITE);
		Matrix4 normalProjection = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight()); //required to render text properly
		batch.setProjectionMatrix(normalProjection);
		
		DrawLoc = new Vector2((DrawLoc.x / Scale) + ( CircleSize/Scale/2 ) - (textWidth/2), (DrawLoc.y/Scale) + ( CircleSize/Scale/2 ) + (textHeight/2));
		
		
		//font.draw(batch, "Drawing to X: "+(int)DrawLoc.x +" Y: "+ (int)DrawLoc.y, 32, 80/Scale);
		if(drawMoveCenter)//Debug Info on Touch:
		{
			font.draw(batch, "Current Touch X: "+(int)currTouchPosition.x+"Y: "+(int)currTouchPosition.y, 32,80/Scale);
			Vector2 SummonPos= startTouchPosition.cpy().add(lowerLeftOfView).div(TILE_SIZE);
			SummonPos.x=(SummonPos.x);//(float) Math.ceil(TILE_SIZE/SummonPos.x);
			SummonPos.y=(SummonPos.y);//(float) Math.ceil(TILE_SIZE/SummonPos.y);
			SummonPos.x=(float)roundUp(SummonPos.x, 1);
			SummonPos.y=(float)roundUp(SummonPos.y, 1);
			font.draw(batch, "Summon Pos X:" +SummonPos.x + " Y: "+SummonPos.y, 256/Scale, 50/Scale);
		}
		//font.scale((float) 2.3);
		bigFont.draw(batch, ((Villagers < 10 ) ? "0": "") +Integer.toString(Villagers), DrawLoc.x, DrawLoc.y);
		
		//this next code was written out in favor of a bar approach
		//bigFont.draw(batch, ((MonstersKilled < 10 ) ? "0": "") +Integer.toString(MonstersKilled), DrawLoc.x, DrawLoc.y-CircleSize);//This line displays monsters till next level upon the screen
		
		font.setColor(Color.BLACK);
		font.draw(batch, str + "Mana: "+GlobalGameData.getPlayer().getMana(), 192/Scale,25);
		//font.draw(batch, str + "DeltaTime: "+deltaTime, 192/Scale,25);
		
		//font.scale((float)-2.3);
	}
	
	private void DrawManaCost(SummonHelper.SummonMode mode, float PosX, float PosY)
	{
		Sprite toDraw=MainGame.getTextureRepo().getUiElement(UiElement.ManaChunk).getStepInCol(0, 0);
		float scale = MainGame.getSummonHelper().getSummonCost(mode);
		float scaleMod = 10;
<<<<<<< HEAD
		float Aspect=GameSettings.getAspectRatio();
		float superScale = 3 * scale/scaleMod * Aspect;
		toDraw.setScale(superScale, Aspect);
=======
		float superScale = 3 * scale/scaleMod;
		toDraw.setScale(superScale, 1);
>>>>>>> 578673f98688ed68727b7a6595659cb12112f2a5
		toDraw.setOrigin(0,0);
		drawAtLocation(toDraw, PosX - ( (toDraw.getRegionWidth() * superScale) ), PosY);
	}
	
	private void DrawHelpNotifications()
	{
		Sprite toDraw;
<<<<<<< HEAD
		Vector2 DrawPos=null;
		float screenPosX = GameSettings.getScreenWidth();
		float screenPosY = GameSettings.getScreenHeight();
		float scale = GameSettings.getAspectRatio();
		if(drawHelp[0]){
			toDraw = MainGame.getTextureRepo().getUiElement(UiElement.HelpNotifyVertical).getStepInRow(0, 0);
			toDraw.setScale(scale);
			DrawPos=new Vector2( 
					( screenPosX/2 )-( toDraw.getRegionWidth()/2 ), 
					( screenPosY - (toDraw.getRegionHeight()) ));
			DrawPos.mul(scale);
			drawAtLocation( toDraw,  DrawPos);
		}
		if(drawHelp[1]){
			toDraw = MainGame.getTextureRepo().getUiElement(UiElement.HelpNotifyVertical).getStepInRow(1,0);
			DrawPos=new Vector2(( screenPosX/2 )-( toDraw.getRegionWidth()/2 ), 0);
			toDraw.setScale(scale);
			DrawPos.mul(scale);
			drawAtLocation(toDraw, DrawPos);
		}
		if(drawHelp[2]){
			toDraw = MainGame.getTextureRepo().getUiElement(UiElement.HelpNotifyHorizontal).getStepInRow(0,0);
			DrawPos= new Vector2(0 , ( screenPosY/2 - (toDraw.getRegionHeight()/2 )));
			toDraw.setScale(scale);
			DrawPos.mul(scale);
			drawAtLocation(toDraw,   DrawPos);
		}
		if(drawHelp[3]){
			toDraw = MainGame.getTextureRepo().getUiElement(UiElement.HelpNotifyHorizontal).getStepInRow(1,0);
			DrawPos= new Vector2((screenPosX - (toDraw.getRegionWidth())) , ( screenPosY/2 - (toDraw.getRegionHeight()/2 )));
			toDraw.setScale(scale);
			DrawPos.mul(scale);
			drawAtLocation(toDraw, DrawPos);
		}
	}
	
	public void villageInTrouble(Entity entity)
=======
		if(drawHelp[0]){
			toDraw = MainGame.getTextureRepo().getUiElement(UiElement.HelpNotifyVertical).getStepInRow(0, 0);
			drawAtLocation( toDraw,  ( GameSettings.getScreenWidth()/2 )-( toDraw.getRegionWidth()/2 ), ( GameSettings.getScreenHeight() - (toDraw.getRegionHeight()) ));
		}
		if(drawHelp[1]){
			toDraw = MainGame.getTextureRepo().getUiElement(UiElement.HelpNotifyVertical).getStepInRow(1,0);
			drawAtLocation(toDraw,  ( GameSettings.getScreenWidth()/2 )-( toDraw.getRegionWidth()/2 ), 0);
		}
		if(drawHelp[2]){
			toDraw = MainGame.getTextureRepo().getUiElement(UiElement.HelpNotifyHorizontal).getStepInRow(0,0);
			drawAtLocation(toDraw,   0 , ( GameSettings.getScreenHeight()/2 - (toDraw.getRegionHeight()/2 )));
		}
		if(drawHelp[3]){
			toDraw = MainGame.getTextureRepo().getUiElement(UiElement.HelpNotifyHorizontal).getStepInRow(1,0);
			drawAtLocation(toDraw,   (GameSettings.getScreenWidth() - (toDraw.getRegionWidth())) , ( GameSettings.getScreenHeight()/2 - (toDraw.getRegionHeight()/2 )));
		}
		
	}
	
	public void villageInTrouble(Village entity)
>>>>>>> 578673f98688ed68727b7a6595659cb12112f2a5
	{
		Vector2 Position=entity.getPosition().cpy().mul(TILE_SIZE);
		Vector2 UpperRight=getUpperRightOfView();
		
<<<<<<< HEAD
		if(!isOnScreen(Position) && !drawGameOver)
		{
			if(Position.y >UpperRight.y){
=======
		if(isOnScreen(Position) && !drawGameOver)
		{
			if(Position.y > UpperRight.y){
>>>>>>> 578673f98688ed68727b7a6595659cb12112f2a5
				setDrawHelp(0, true);
			}
			if(Position.y < lowerLeftOfView.y){
				setDrawHelp(1, true);
			}
			if(Position.x < lowerLeftOfView.x){
				setDrawHelp(2, true);
			}
			if(Position.x > UpperRight.x){
				setDrawHelp(3, true);
			}
		}
	}
	
	public void setDrawHelp(int sector, boolean value){
		drawHelp[sector]=value;
	}
<<<<<<< HEAD
	
	private int interval[]={0,0,0,0};
	public void ResetDrawHelp(){
		
		for(int i =0; i<4;i++){
			if(drawHelp[i]){
				interval[i]++;
				if(interval[i]>90){
					System.out.println("Resetting Help: "+i);
					interval[i]=0;
					drawHelp[i]=false;
				}
			}
=======
	public void ResetDrawHelp(){
		for(int i =0; i<4;i++){
			setDrawHelp(i, false);
>>>>>>> 578673f98688ed68727b7a6595659cb12112f2a5
		}
	}
	
	public void setDrawGameOver(boolean value){
		drawGameOver=value;
	}	
	
	public void setDrawLevelUp(boolean value){
		drawLevelUp=value;
		if(drawLevelUp==false){overlayFadeIn=0;}
	}
	
	public void setOverlayFadeOut(boolean value){
		OverlayFade=value;
		if(drawGameOver){OverlayFade=false;}
	}
	
	private void drawOverlay(int type)
	{
		int Overlay_dim = 256;//overlay dimensions(its a square)
		Sprite overlay=null;
		switch(type)
		{
		case 1://Draw Game Over Overlay
			overlay=MainGame.getTextureRepo().getOverlays(ScreenOverlay.GameOver).getStepInRow(0, 0);
			break;
		case 2://Draw Level Up Overlay
			overlay=MainGame.getTextureRepo().getOverlays(ScreenOverlay.LevelUp).getStepInRow(0,0);
			break;
		
		}
		float width  = (GameSettings.getScreenWidth()/Overlay_dim) * GameSettings.getAspectRatio();
		float height = (GameSettings.getScreenHeight()/Overlay_dim) * GameSettings.getAspectRatio();
		
		overlay.setScale(width, height);
		
		if(overlayFadeIn < 1 && !OverlayFade){//30 frames per second at .01+ per frame = a fade in of ~3 seconds
			overlayFadeIn+=.01;
			//System.out.println("Scale: "+width +" / "+height);
		}
		else if(overlayFadeIn > 0)
		{
			overlayFadeIn-=.01;
		}
		if(overlayFadeIn < 0  && OverlayFade==true){overlayFadeIn=0; OverlayFade=false;drawLevelUp=false;}
		if(overlayFadeIn>1){overlayFadeIn = 1;}
			Color fadeValue = overlay.getColor();
			fadeValue.a=overlayFadeIn;
			overlay.setColor(fadeValue);
			Vector2 DrawLoc = new Vector2(0,0);
			overlay.setOrigin(0,0);
			drawAtLocation(overlay, DrawLoc);
	}
	
	private void drawAtLocation(Sprite sprite, Vector2 position) {
		drawAtLocation(sprite, position.x, position.y);
	}
	private void drawAtLocation(Sprite sprite, float x, float y) {
		//This weirdness is related to how aspect ratio is handled
		sprite.setPosition(x/GameSettings.getScreenHeight( ),y/GameSettings.getScreenHeight( ));
		sprite.draw(batch);
	}
	private void setupDisplay( ) {
		GL10 gl = Gdx.graphics.getGL10();
		gl.glViewport(0, 0, (int)GameSettings.getScreenWidth( ), (int)GameSettings.getScreenHeight( ));	
		
		camera.position.set(0.5f, GameSettings.getScreenHeight() / GameSettings.getScreenWidth() / 2, 0);		
		
		camera.update();
		camera.apply(gl);
		
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
	}
	
	public void moveView(Vector2 movementInTiles) {
		lowerLeftOfView.add(movementInTiles.cpy().mul(TILE_SIZE));
		
		float maxX = map.getWidth() * TILE_SIZE - (GameSettings.getScreenWidth( ));
		float maxY = map.getHeight() * TILE_SIZE - (GameSettings.getScreenHeight( ));
		
		GameTools.clamp(lowerLeftOfView, new Vector2(0, 0), new Vector2(maxX, maxY));
	}

	public void dispose() {
		batch.dispose();
	}

	public void setDrawMoveCenter(boolean draw) {
		drawMoveCenter = draw;
		
	}
	double roundUp(double x, double f) {
		  return f * Math.ceil(x / f);
		}

	boolean MagePlayed=false;
	boolean WarrPlayed=false;
	boolean ArchPlayed=false;
	public boolean setButtonDraw()//Also handles summoning units. Move functionality elsewhere?
	{
		//0, 1, 2, 3 | All, Warrior, Archer, Mage
		//float Scale=GameSettings.getAspectRatio();
		boolean draw=false;
		float touchX = startTouchPosition.x;
		float touchY = startTouchPosition.y;
		float buttonDim = 64 * GameSettings.getAspectRatio();
		
<<<<<<< HEAD
		if(touchX < (buttonDim *4) && touchY < (buttonDim ))//touch coordinates are (not?) affected by aspect ratio
=======
		if(touchX < (64 *4) && touchY < (64 ))//touch coordinates are (not?) affected by aspect ratio
>>>>>>> 578673f98688ed68727b7a6595659cb12112f2a5
		{
			if(touchX < buttonDim)
			{
				//set Warrior
				if(GlobalGameData.getPlayer().getMana() >= MainGame.getSummonHelper().getSummonCost(SummonHelper.SummonMode.Warrior))
				{
					System.out.println("!Warrior Selected");
					MainGame.getSummonHelper().setSummonMode(SummonHelper.SummonMode.Warrior);
				}
				else{MainGame.getSoundHelper().playSound(Sounds.BadSelection);}
			}
			else if(touchX < (buttonDim*2))
			{
				//set Archer
				if(GlobalGameData.getPlayer().getMana() >= MainGame.getSummonHelper().getSummonCost(SummonHelper.SummonMode.Archer))
				{
					System.out.println("!Archer Seleted");
					MainGame.getSummonHelper().setSummonMode(SummonHelper.SummonMode.Archer);
				}
				else{MainGame.getSoundHelper().playSound(Sounds.BadSelection);}
			}
<<<<<<< HEAD
			else if(touchX < (buttonDim*3))
=======
			else if(touchX < (64*3))
>>>>>>> 578673f98688ed68727b7a6595659cb12112f2a5
			{
				//set Mage
				
				if(GlobalGameData.getPlayer().getMana() >= MainGame.getSummonHelper().getSummonCost(SummonHelper.SummonMode.Mage))
				{
					System.out.println("!Mage Selected");
					MainGame.getSummonHelper().setSummonMode(SummonHelper.SummonMode.Mage);
					if(!MagePlayed)
					{
						MagePlayed=true;
						//MainGame.getSoundHelper().playSound(Sounds.Mage);
					}
				}
				else{MainGame.getSoundHelper().playSound(Sounds.BadSelection);}
			}
			else	//NEW: God Attack
			{
<<<<<<< HEAD
				if(GlobalGameData.getPlayer().getGodAttackPercent() == 0)
=======
				if(GlobalGameData.getPlayer().getGodAttackPercent() == 1)
>>>>>>> 578673f98688ed68727b7a6595659cb12112f2a5
				{
					System.out.println("!God Attack Selected");
					MainGame.getSummonHelper().setSummonMode(SummonHelper.SummonMode.GodAttack);
				}
				else{MainGame.getSoundHelper().playSound(Sounds.BadSelection);}
			}
			draw=true;
		}
		else
		{
			if(MainGame.getSummonHelper().getSummonMode() != SummonHelper.SummonMode.None)
			{
				Vector2 SummonPos= startTouchPosition.cpy().add(lowerLeftOfView).div(TILE_SIZE);
				/*SummonPos.x=(SummonPos.x);//(float) Math.ceil(TILE_SIZE/SummonPos.x);
				SummonPos.y=(SummonPos.y);//(float) Math.ceil(TILE_SIZE/SummonPos.y);
				SummonPos.x=(float)roundUp(SummonPos.x, 1);
				SummonPos.y=(float)roundUp(SummonPos.y, 1);*/
				SummonPos.x=(float)Math.floor(SummonPos.x);
				SummonPos.y=(float)Math.floor(SummonPos.y);
				
				if(!MainGame.getSummonHelper().SummonAtPos(SummonPos))    //ToDo: Implement: done
				{
					setDrawBadSelection(true);
				}
				draw=true;
			}
			MainGame.getSummonHelper().setSummonMode(SummonHelper.SummonMode.None);
		}
		return draw;
	}
		
	
	
	public void setDrawBadSelection(boolean draw)
	{
		this.drawBadSelection=draw;
	}
	public void DrawBadSelection()
	{
		this.dispError++;	//the counter for how long to display the sign
		//Vector2 monsterPos = entity.getPosition().cpy().mul(TILE_SIZE).sub(lowerLeftOfView);
		if(BadSelectionVect==null)
		{
			Vector2 temp= startTouchPosition.cpy().add(lowerLeftOfView).div(TILE_SIZE);
			System.out.println("Bad Draw Pos (TILES): "+temp.toString());
			temp.x=(float)Math.floor(temp.x);
			temp.y=(float)Math.floor(temp.y);
			System.out.println("Bad Draw Pos (TILES FLOORED): "+temp.toString());
			BadSelectionVect=temp;
			MainGame.getSoundHelper().playSound(Sounds.BadSelection);
		}
		if(this.dispError==60)
		{
			setDrawBadSelection(false);
			this.dispError=0;
			BadSelectionVect=null;
		}
		else
		{
			
			Vector2 drawPos = BadSelectionVect.cpy().mul(TILE_SIZE).sub(lowerLeftOfView);
			Sprite toDraw = MainGame.getTextureRepo().getUiElement(UiElement.Circles).getStepInRow(1,1);
			toDraw.setScale((float).5,(float).5);
			drawAtLocation(toDraw, drawPos);
			
		}
	}
	
	public void setStartTouch(Vector2 startTouchPoint) {
		startTouchPosition = startTouchPoint.cpy( );		
		startTouchPosition.y = GameSettings.getScreenHeight() - startTouchPosition.y;
		startTouchPosition.mul(GameSettings.getAspectRatio());
	}
	public void setCurrTouch(Vector2 startTouchPoint) {
		currTouchPosition = startTouchPoint.cpy( );		
		currTouchPosition.y = GameSettings.getScreenHeight() - currTouchPosition.y;
		currTouchPosition.mul(GameSettings.getAspectRatio());
	}
	
	private Vector2 getUpperRightOfView() {
		return lowerLeftOfView.cpy().add(new Vector2(GameSettings.getScreenWidth( ), GameSettings.getScreenHeight( )));	
	}
}
