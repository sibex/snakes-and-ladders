package snakesladders.domain.services

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import snakesladders.domain.models.Players.Player
import snakesladders.domain.services.Dice.{DiceRolled, Roll}
import snakesladders.domain.services.GameService.RollDiceForPlayer
import snakesladders.domain.services.GameStateMachine.{InitializeGame, PlayerRolled}

class GameServiceSpecs
  extends TestKit(ActorSystem("GameServiceSpecs"))
    with ImplicitSender
    with WordSpecLike
    with Matchers
    with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "GameService" when {

    "started" should {
      "initialize the game" in {
        val service = system.actorOf(GameService.props(testActor, testActor))

        expectMsg(InitializeGame)
      }
    }

    "sending RollDiceForPlayer" should {
      "roll the dice" in {
        val service = system.actorOf(GameService.props(testActor, testActor))
        val player = Player("p1")

        service ! RollDiceForPlayer(player)
        expectMsg(InitializeGame)
        expectMsg(Roll(player))
      }
    }

    "sending DiceRolled" should {
      "send PlayerRolled to game state" in {
        val service = system.actorOf(GameService.props(testActor, testActor))
        val player = Player("p1")

        service ! DiceRolled(player, 6)
        expectMsg(InitializeGame)
        expectMsg(PlayerRolled(player, 6))
      }
    }
  }

}