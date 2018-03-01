package src.model.packet;

public enum Action {
    LOGIN,
    //Передаётся с помощью массива
    GET_ALL_TASKS,
    ADD_OR_UPDATE_TASK, DELETE_TASK
}
