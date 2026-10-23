import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
public class SimpleKeyboardService extends InputMethodService {
    private final List<TextView> letterKeys = new ArrayList<>();
    private boolean isShift = false;
    private boolean isCapsLock = false;
    private int currentLayoutResId = R.layout.keyboard_view;
    private int currentLanguageLayoutResId = R.layout.keyboard_view;
    private View currentInputView;
    private EditorInfo currentEditorInfo;
    private final Handler deleteHandler = new Handler(Looper.getMainLooper());
    private Runnable deleteRunnable;
    @Override
    public View onCreateInputView() {
        return createKeyboardView(currentLayoutResId);
    }
    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        currentEditorInfo = info;
        updateEnterButtonVisibility(info);
    }
    private View createKeyboardView(int layoutResId) {
        currentLayoutResId = layoutResId;
        View keyboardView = getLayoutInflater().inflate(layoutResId, null);
        currentInputView = keyboardView;
        letterKeys.clear();
        isShift = false;
        isCapsLock = false;
        setupKey(keyboardView, R.id.btn_1, "1");
        setupKey(keyboardView, R.id.btn_2, "2");
        setupKey(keyboardView, R.id.btn_3, "3");
        setupKey(keyboardView, R.id.btn_4, "4");
        setupKey(keyboardView, R.id.btn_5, "5");
        setupKey(keyboardView, R.id.btn_6, "6");
        setupKey(keyboardView, R.id.btn_7, "7");
        setupKey(keyboardView, R.id.btn_8, "8");
        setupKey(keyboardView, R.id.btn_9, "9");
        setupKey(keyboardView, R.id.btn_0, "0");
        if (layoutResId == R.layout.keyboard_english) {
            setupLetterKey(keyboardView, R.id.btn_q, "q");
            setupLetterKey(keyboardView, R.id.btn_w, "w");
            setupLetterKey(keyboardView, R.id.btn_e, "e");
            setupLetterKey(keyboardView, R.id.btn_r, "r");
            setupLetterKey(keyboardView, R.id.btn_t, "t");
            setupLetterKey(keyboardView, R.id.btn_y, "y");
            setupLetterKey(keyboardView, R.id.btn_u, "u");
            setupLetterKey(keyboardView, R.id.btn_i, "i");
            setupLetterKey(keyboardView, R.id.btn_o, "o");
            setupLetterKey(keyboardView, R.id.btn_p, "p");
            setupLetterKey(keyboardView, R.id.btn_a, "a");
            setupLetterKey(keyboardView, R.id.btn_s, "s");
            setupLetterKey(keyboardView, R.id.btn_d, "d");
            setupLetterKey(keyboardView, R.id.btn_f, "f");
            setupLetterKey(keyboardView, R.id.btn_g, "g");
            setupLetterKey(keyboardView, R.id.btn_h, "h");
            setupLetterKey(keyboardView, R.id.btn_j, "j");
            setupLetterKey(keyboardView, R.id.btn_k, "k");
            setupLetterKey(keyboardView, R.id.btn_l, "l");
            setupLetterKey(keyboardView, R.id.btn_z, "z");
            setupLetterKey(keyboardView, R.id.btn_x, "x");
            setupLetterKey(keyboardView, R.id.btn_c, "c");
            setupLetterKey(keyboardView, R.id.btn_v, "v");
            setupLetterKey(keyboardView, R.id.btn_b, "b");
            setupLetterKey(keyboardView, R.id.btn_n, "n");
            setupLetterKey(keyboardView, R.id.btn_m, "m");
        } else if (layoutResId == R.layout.keyboard_view) {
            setupLetterKey(keyboardView, R.id.btn_j, "й");
            setupLetterKey(keyboardView, R.id.btn_ts, "ц");
            setupLetterKey(keyboardView, R.id.btn_u, "у");
            setupLetterKey(keyboardView, R.id.btn_k, "к");
            setupLetterKey(keyboardView, R.id.btn_e, "е");
            setupLetterKey(keyboardView, R.id.btn_n, "н");
            setupLetterKey(keyboardView, R.id.btn_g, "г");
            setupLetterKey(keyboardView, R.id.btn_sh, "ш");
            setupLetterKey(keyboardView, R.id.btn_sch, "щ");
            setupLetterKey(keyboardView, R.id.btn_z, "з");
            setupLetterKey(keyboardView, R.id.btn_h, "х");
            setupLetterKey(keyboardView, R.id.btn_hard, "ъ");
            setupLetterKey(keyboardView, R.id.btn_f, "ф");
            setupLetterKey(keyboardView, R.id.btn_y_soft, "ы");
            setupLetterKey(keyboardView, R.id.btn_v, "в");
            setupLetterKey(keyboardView, R.id.btn_a, "а");
            setupLetterKey(keyboardView, R.id.btn_p, "п");
            setupLetterKey(keyboardView, R.id.btn_r, "р");
            setupLetterKey(keyboardView, R.id.btn_o, "о");
            setupLetterKey(keyboardView, R.id.btn_l, "л");
            setupLetterKey(keyboardView, R.id.btn_d, "д");
            setupLetterKey(keyboardView, R.id.btn_zh, "ж");
            setupLetterKey(keyboardView, R.id.btn_ee, "э");
            setupLetterKey(keyboardView, R.id.btn_yo, "ё");
            setupLetterKey(keyboardView, R.id.btn_ya, "я");
            setupLetterKey(keyboardView, R.id.btn_ch, "ч");
            setupLetterKey(keyboardView, R.id.btn_s, "с");
            setupLetterKey(keyboardView, R.id.btn_m, "м");
            setupLetterKey(keyboardView, R.id.btn_i, "и");
            setupLetterKey(keyboardView, R.id.btn_t, "т");
            setupLetterKey(keyboardView, R.id.btn_soft, "ь");
            setupLetterKey(keyboardView, R.id.btn_b, "б");
            setupLetterKey(keyboardView, R.id.btn_yu, "ю");
        }
        View btnTop = keyboardView.findViewById(R.id.btn_top);
        if (btnTop != null) {
            btnTop.setOnClickListener(v -> {
                if (isCapsLock) {
                    isCapsLock = false;
                    setShift(false);
                } else {
                    setShift(!isShift);
                }
            });
            btnTop.setOnLongClickListener(v -> {
                isCapsLock = !isCapsLock;
                if (isCapsLock) {
                    Toast.makeText(this, "Caps Lock включен", Toast.LENGTH_SHORT).show();
                    setShift(true);
                } else {
                    Toast.makeText(this, "Caps Lock выключен", Toast.LENGTH_SHORT).show();
                    setShift(false);
                }
                return true;
            });
        }
        View btnSymbol = keyboardView.findViewById(R.id.btn_sumbol);
        if (btnSymbol != null) {
            btnSymbol.setOnClickListener(v -> {
                if (currentLayoutResId == R.layout.symbol) {
                    setInputView(createKeyboardView(currentLanguageLayoutResId));
                } else {
                    setInputView(createKeyboardView(R.layout.symbol));
                }
            });
        }
        View btnLanguage = keyboardView.findViewById(R.id.btn_language);
        if (btnLanguage != null) {
            btnLanguage.setOnClickListener(v -> {
                if (currentLanguageLayoutResId == R.layout.keyboard_view) {
                    currentLanguageLayoutResId = R.layout.keyboard_english;
                } else {
                    currentLanguageLayoutResId = R.layout.keyboard_view;
                }
                setInputView(createKeyboardView(currentLanguageLayoutResId));
            });
        }
        View btnSetting = keyboardView.findViewById(R.id.btn_setting);
        if (btnSetting != null) {
            btnSetting.setOnClickListener(v -> {
                Intent intent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });
        }
        setupAllTextViews(keyboardView);
        View btnSpace = keyboardView.findViewById(R.id.btn_space);
        if (btnSpace != null) {
            btnSpace.setOnClickListener(v -> {
                InputConnection ic = getCurrentInputConnection();
                if (ic != null) {
                    ic.commitText(" ", 1);
                }
            });
        }
        View btnDelete = keyboardView.findViewById(R.id.btn_delete);
        if (btnDelete != null) {
            deleteRunnable = new Runnable() {
                @Override
                public void run() {
                    InputConnection ic = getCurrentInputConnection();
                    if (ic != null) {
                        ic.deleteSurroundingText(1, 0);
                    }
                    deleteHandler.postDelayed(this, 100);
                }
            };
            btnDelete.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setPressed(true);
                        InputConnection ic = getCurrentInputConnection();
                        if (ic != null) {
                            ic.deleteSurroundingText(1, 0);
                        }
                        deleteHandler.removeCallbacks(deleteRunnable);
                        deleteHandler.postDelayed(deleteRunnable, 400);
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.performClick();
                    case MotionEvent.ACTION_CANCEL:
                        v.setPressed(false);
                        deleteHandler.removeCallbacks(deleteRunnable);
                        return true;
                }
                return false;
            });
        }
        View btnEnter = keyboardView.findViewById(R.id.btn_enter);
        if (btnEnter != null) {
            btnEnter.setOnClickListener(v -> {
                InputConnection ic = getCurrentInputConnection();
                if (ic != null) {
                    ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                    ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
                }
            });
        }
        View btnEnterSearch = keyboardView.findViewById(R.id.btn_enter_search);
        if (btnEnterSearch != null) {
            btnEnterSearch.setOnClickListener(v -> {
                InputConnection ic = getCurrentInputConnection();
                if (ic != null) {
                    boolean handled = ic.performEditorAction(EditorInfo.IME_ACTION_SEARCH);
                    if (!handled) {
                        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
                    }
                }
            });
        }
        updateEnterButtonVisibility(currentEditorInfo);
        return keyboardView;
    }
    private void updateEnterButtonVisibility(EditorInfo attribute) {
        if (currentInputView == null) return;
        View btnEnter = currentInputView.findViewById(R.id.btn_enter);
        View btnEnterSearch = currentInputView.findViewById(R.id.btn_enter_search);
        if (btnEnter == null || btnEnterSearch == null) return;
        btnEnter.setVisibility(View.VISIBLE);
        if (attribute != null) {
            int action = attribute.imeOptions & EditorInfo.IME_MASK_ACTION;
            boolean isSearch = (action == EditorInfo.IME_ACTION_SEARCH)
                    || (attribute.actionId == EditorInfo.IME_ACTION_SEARCH);
            if (isSearch) {
                btnEnterSearch.setVisibility(View.VISIBLE);
            } else {
                btnEnterSearch.setVisibility(View.GONE);
            }
        } else {
            btnEnterSearch.setVisibility(View.GONE);
        }
    }
    private void setupAllTextViews(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                setupAllTextViews(group.getChildAt(i));
            }
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            int id = tv.getId();
            if (id != R.id.btn_sumbol && id != R.id.btn_language && id != R.id.btn_setting
                    && id != R.id.btn_delete && id != R.id.btn_top && id != R.id.btn_space
                    && id != R.id.btn_enter && id != R.id.btn_enter_search && !letterKeys.contains(tv)) {
                if (!tv.hasOnClickListeners()) {
                    tv.setOnClickListener(v -> {
                        InputConnection ic = getCurrentInputConnection();
                        if (ic != null) {
                            ic.commitText(tv.getText().toString(), 1);
                        }
                    });
                }
            }
        }
    }
    @Override
    public void onFinishInputView(boolean finishingInput) {
        super.onFinishInputView(finishingInput);
        if (deleteRunnable != null) {
            deleteHandler.removeCallbacks(deleteRunnable);
        }
    }
    private void setupKey(View parentView, int buttonId, String text) {
        View button = parentView.findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(v -> {
                InputConnection ic = getCurrentInputConnection();
                if (ic != null) {
                    ic.commitText(text, 1);
                }
            });
        }
    }
    private void setupLetterKey(View parentView, int buttonId, String defaultText) {
        TextView tv = parentView.findViewById(buttonId);
        if (tv != null) {
            tv.setText(defaultText);
            letterKeys.add(tv);
            tv.setOnClickListener(v -> {
                InputConnection ic = getCurrentInputConnection();
                if (ic != null) {
                    ic.commitText(tv.getText().toString(), 1);
                }
                if (isShift && !isCapsLock) {
                    setShift(false);
                }
            });
        }
    }
    private void setShift(boolean active) {
        isShift = active;
        for (TextView tv : letterKeys) {
            String current = tv.getText().toString();
            if (active) {
                tv.setText(current.toUpperCase());
            } else {
                tv.setText(current.toLowerCase());
            }
        }
    }
}
