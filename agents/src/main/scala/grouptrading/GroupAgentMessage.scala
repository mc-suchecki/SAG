package grouptrading

import common.AgentMessage

/** Super-type for messages exchanged between GroupMaster and GroupAgents. */
sealed trait GroupAgentMessage extends AgentMessage
case class Buy(partOfCashToSpend: Double, price: Double) extends GroupAgentMessage
case class Sell(partOfStocksToSell: Double, price: Double) extends GroupAgentMessage

