/*
 * Copyright (C) 2017  Ľuboš Kozmon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.elkozmon.zoonavigator.core.action.actions

import com.elkozmon.zoonavigator.core.action.ActionHandler
import com.elkozmon.zoonavigator.core.curator.BackgroundOps
import com.elkozmon.zoonavigator.core.zookeeper.znode._
import org.apache.curator.framework.CuratorFramework

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.Future

class GetZNodeDataActionHandler(
    curatorFramework: CuratorFramework,
    implicit val executionContextExecutor: ExecutionContextExecutor
) extends ActionHandler[GetZNodeDataAction]
    with BackgroundOps {

  override def handle(
      action: GetZNodeDataAction
  ): Future[ZNodeMetaWith[ZNodeData]] =
    curatorFramework.getData
      .forPathBackground(action.path.path)
      .map { event =>
        val meta = ZNodeMeta.fromStat(event.getStat)
        val data = ZNodeData(Option(event.getData).getOrElse(Array.empty))

        ZNodeMetaWith(data, meta)
      }
}