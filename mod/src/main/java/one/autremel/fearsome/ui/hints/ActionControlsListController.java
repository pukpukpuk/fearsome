package one.autremel.fearsome.ui.hints;

import com.google.common.base.Preconditions;

class ActionControlsListController {
    private static boolean initialized;

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        //TODO нужен общий енам со всеми действиями, на серваке он будет пустым,
        // на клиенте будет содержать ключ к бинду и ключ к названию действия.
        // пэйлоада надо два:
        //  регистрация - упорядоченный массив всех действий - повлечет создание под каждое действие лабеля
        //  установка видимости - изменит видимость для зарегистрированного действия
    }


}
