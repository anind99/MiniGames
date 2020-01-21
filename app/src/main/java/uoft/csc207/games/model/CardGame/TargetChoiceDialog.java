package uoft.csc207.games.model.CardGame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

public class TargetChoiceDialog extends AppCompatDialogFragment {

    private TargetChoiceDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        CharSequence[] allChoices = new CharSequence[4];
        allChoices[0] = "Direct Attack";
        allChoices[1] = "Left Card";
        allChoices[2] = "Middle Card";
        allChoices[3] = "Right Card";
        builder.setTitle("Select a target")
                .setItems(allChoices, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                listener.onOtherPlayerClicked();
                                break;
                            case 1:
                                listener.onLeftCardClicked();
                                break;
                            case 2:
                                listener.onMiddleCardClicked();
                                break;
                            case 3:
                                listener.onRightCardClicked();
                                break;
                        }
                    }
                });
        return builder.create();
    }

    public interface TargetChoiceDialogListener {
        void onOtherPlayerClicked();
        void onLeftCardClicked();
        void onMiddleCardClicked();
        void onRightCardClicked();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (TargetChoiceDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement TargetDialogListener");
        }
    }
}
