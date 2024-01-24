package lila.relay

import lila.notify.BroadcastRound

final private class RelayNotifier(
    notifyApi: lila.notify.NotifyApi,
    tourRepo: RelayTourRepo
)(using Executor):

  def roundBegin(rt: RelayRound.WithTour): Funit =
    tourRepo.subscribers(rt.tour.id) flatMap: subscribers =>
      subscribers.nonEmpty so:
        notifyApi.notifyMany(
          subscribers,
          BroadcastRound(
            rt.path,
            s"${rt.tour.name} ${rt.round.name} has begun",
            none
          )
        )
