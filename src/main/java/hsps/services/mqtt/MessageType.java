package hsps.services.mqtt;

public enum MessageType {

	// Default-MessageTypes
	InitGame, GameRunning, EndedGame, PauseGame, RestartGame, ChooseCard, PlayerTopic, GetCard, InvalidCard, ValidCard, PlayerGotStich,

	// Announcement-MessageTypes
	Announcement, AskReContraAnnouncement,
	
	// DecisionRule-MessageTypes
	AskKoenigSolo, AskSchmeissen,
	
	// Rule-MessageTypes
	Armut, KoenigSolo, Schmeissen, Schweinchen
}
