package optimalPlay;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.EndTurnAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpirePatch(clz= GameActionManager.class, method = "useNextCombatActions")
public class PreUseNextCombatActionsHook {
	@SpirePrefixPatch
	public static void preUseNextCombatActionsHook(GameActionManager __instance) {
		Logger logger = LogManager.getLogger(DefaultMod.class.getName());
		logger.info("preUseNextCombatActions");

		AbstractDungeon.actionManager.addToNextCombat(new AbstractGameAction() {
			@Override
			public void update() {
				AbstractPlayer player = AbstractDungeon.player;
				AbstractCard card = player.hand.findCardById("Strike_P");
				AbstractRoom room = AbstractDungeon.getCurrRoom();

				if (card != null) {
					AbstractMonster monster = room.monsters.monsters.get(0);
					logger.info("Monster: " + monster.name);
					AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(card, monster));
					logger.info("Tried to play card: " + card.name);
				}

				AbstractDungeon.overlayMenu.endTurnButton.disable(true);

				this.isDone = true;
			}
		});
	}
}
