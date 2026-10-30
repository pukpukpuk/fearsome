package one.autremel.fearsome.network;

import java.util.List;

public class Payloads {
    public static final List<ServerPayloadId<?>> SERVER_PAYLOAD_IDS = List.of(
			one.autremel.fearsome.ui.game_state.payloads.PlayAbilityProgressAnimationPayload.ID,
			one.autremel.fearsome.network.ping.PongPayload.ID,
			one.autremel.fearsome.effects.SetDisplayedEffectArgsPayload.ID,
			one.autremel.fearsome.ui.survivor_list.payloads.CreateSurvivorListPayload.ID,
			one.autremel.fearsome.ui.game_state.payloads.UnmarkGeneratorBlockPayload.ID,
			one.autremel.fearsome.ui.hints.payloads.UnregisterAllActionControlsPayload.ID,
			one.autremel.fearsome.ui.terror.SetCurrentTerrorBoundPayload.ID,
			one.autremel.fearsome.illusions.SetDisplayNameVisibilityPayload.ID,
			one.autremel.fearsome.ui.game_state.payloads.SetAbilityActiveStatePayload.ID,
			one.autremel.fearsome.ui.hints.payloads.SetActionVisibilityPayload.ID,
			one.autremel.fearsome.network.RemoveScreensPayload.ID,
			one.autremel.fearsome.effects.RemoveDisplayedEffectPayload.ID,
			one.autremel.fearsome.ui.game_state.payloads.MarkBlocksAsGeneratorsPayload.ID,
			one.autremel.fearsome.ui.game_state.payloads.SetGeneratorsCountPayload.ID,
			one.autremel.fearsome.ui.hints.payloads.RegisterActionPayload.ID,
			one.autremel.fearsome.ui.hints.payloads.RegisterActionControlPayload.ID,
			one.autremel.fearsome.effects.AddDisplayedEffectPayload.ID,
			one.autremel.fearsome.ui.hints.payloads.SetActionProgressAnimationPayload.ID,
			one.autremel.fearsome.ui.game_state.payloads.SetAbilityCountPayload.ID,
			one.autremel.fearsome.ui.lobby.CreateLobbyScreenPayload.ID,
			one.autremel.fearsome.ui.terror.CreateTerrorIndicatorScreenPayload.ID,
			one.autremel.fearsome.timer.TimerUpdatePayload.ID,
			one.autremel.fearsome.ui.game_state.payloads.CreateGameStateScreenPayload.ID,
			one.autremel.fearsome.ui.lobby.SetLobbyPlayerCountPayload.ID,
			one.autremel.fearsome.ui.survivor_list.payloads.SetSurvivorDeadStatePayload.ID,
			one.autremel.fearsome.ui.hints.payloads.SetActionControlCooldownPayload.ID,
			one.autremel.fearsome.illusions.SetSelectedItemPayload.ID,
			one.autremel.fearsome.ui.hints.payloads.SetActionProgressParametersPayload.ID,
			one.autremel.fearsome.ui.game_state.payloads.SetQuestsCountPayload.ID,
			one.autremel.fearsome.ui.hints.payloads.SetActionControlActiveStatePayload.ID,
			one.autremel.fearsome.ui.survivor_list.payloads.SetSurvivorHealthAmountPayload.ID
    );

    public static final List<ClientPayloadId<?>> CLIENT_PAYLOAD_IDS = List.of(
			one.autremel.fearsome.input.InputPayload.ID,
			one.autremel.fearsome.network.ping.PingPayload.ID
    );
}

