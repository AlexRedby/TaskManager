package src.common.model.packet;

public enum State {
    OK,
    ERROR, LOGIN_ERROR, PASSWORD_ERROR,
    //Сервер возвращает если у клиента нет ещё задач
    NO_TASKS
}
