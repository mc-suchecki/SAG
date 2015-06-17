package common

/** Super-type for all messages sent in the application. */
trait AgentMessage

/** Message sent to actors telling them to start running. */
case object Init extends AgentMessage
/** Message sent to self, triggered every n seconds. */
case object Trade extends AgentMessage
