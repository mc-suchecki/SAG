package grouptrading

import common.AgentMessage

/** Super-type for messages exchanged between GroupMaster and GroupAgents. */
sealed trait GroupAgentMessage extends AgentMessage
case object Buy extends GroupAgentMessage
case object Sell extends GroupAgentMessage

