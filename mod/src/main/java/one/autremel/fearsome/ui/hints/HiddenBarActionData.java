package one.autremel.fearsome.ui.hints;


import one.autremel.fearsome.ui.components.LinearBar;

public class HiddenBarActionData extends SimpleActionData {

    public HiddenBarActionData(int actionId, int priority, String text) {
        super(actionId, priority, text);
    }

    @Override
    protected void onUpdateInternal(HintsScreen screen) {
        screen.setProgressBarColor(LinearBar.DEFAULT_PROGRESS_COLOR);
        screen.setProgressBarVisible(false);
    }
}
