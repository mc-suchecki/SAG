package common

/** Supertype for all messages sent in the application. */
trait AgentMessage

/** Message sent to actors telling them to start running. */
case object Init extends AgentMessage
