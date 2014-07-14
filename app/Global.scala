import com.pubnub.api.{PubnubError, Callback, Pubnub}
import play.api._



object Global extends GlobalSettings {


  override def onStart(app: Application) {
    Logger.info("Application has started")

    // start pubnub listener
    PubNub.start

  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }
}

object PubNub {
  private var pubnub: Option[Pubnub] = None

  def start {
    Logger.info("connecting pubnub: ")
    pubnub = connect
    Logger.info(pubnub.toString)

    Logger.info("subscribing to broadcast channel")
    var channel = subscribe("goatland")
  }

  private def connect(): Option[Pubnub] = {
    for {
      config <- Play.current.configuration.getConfig("pubnub")
      publishKey <- config.getString("publish_key")
      subscribeKey <- config.getString("subscribe_key")
      secretKey <- config.getString("secret_key")
    } yield new Pubnub(publishKey, subscribeKey, secretKey)
  }

  case class Subscrib

  def subscribe(channelName: String) {

    try {
      pubnub.get.subscribe(channelName, new Callback() {
        override def connectCallback(channel: String, message: Object) {
          Logger.info("SUBSCRIBE : CONNECT on channel:" + channel
            + " : " + message.getClass() + " : "
            + message.toString())
        }

        override def disconnectCallback(channel: String, message: Object) {
          Logger.info("SUBSCRIBE : DISCONNECT on channel:" + channel
            + " : " + message.getClass() + " : "
            + message.toString())
        }

        override def reconnectCallback(channel: String, message: Object) {
          Logger.info("SUBSCRIBE : RECONNECT on channel:" + channel
            + " : " + message.getClass() + " : "
            + message.toString())
        }

        override def successCallback(channel: String, message: Object) {
          Logger.info("SUBSCRIBE : " + channel + " : "
            + message.getClass() + " : " + message.toString())
        }

        override def errorCallback(channel: String, error: PubnubError) {
          Logger.info("SUBSCRIBE : ERROR on channel " + channel
            + " : " + error.toString())
          error.errorCode match {
            case PubnubError.PNERR_FORBIDDEN => { pubnub.get.unsubscribe(channel);}
            case PubnubError.PNERR_UNAUTHORIZED => {pubnub.get.unsubscribe(channel);}
            case _ => {}
          }
        }
      })

    } catch {
      case e: Exception => {}

    }
  }
}
