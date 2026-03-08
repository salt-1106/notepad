package com.example.xiangmuke;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xiangmuke.database.MemoDao;
import com.example.xiangmuke.models.Memo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditMemoActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText contentEditText;
    private Spinner categorySpinner;
    private TimePicker timePicker;
    private Button saveButton;
    private boolean isEditMode;
    private Memo memoToEdit;

    private static final String TAG = "AddEditMemoActivity";
    private static final String[] categories = {"工作", "生活", "学习"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_memo);

        titleEditText = findViewById(R.id.et_title);
        contentEditText = findViewById(R.id.et_content);
        categorySpinner = findViewById(R.id.sp_category);
        timePicker = findViewById(R.id.tp_reminder_time);
        saveButton = findViewById(R.id.btn_save);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);


        memoToEdit = getIntent().getParcelableExtra("memo");
        if (memoToEdit != null) {
            isEditMode = true;
            populateFields(memoToEdit);
        } else {
            isEditMode = false;
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMemo();
            }
        });
    }


    private void populateFields(Memo memo) {
        try {
            titleEditText.setText(memo.getTitle());
            contentEditText.setText(memo.getContent());


            for (int i = 0; i < categories.length; i++) {
                if (categories[i].equals(memo.getCategory())) {
                    categorySpinner.setSelection(i);
                    break;
                }
            }


            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date reminderTime = sdf.parse(memo.getReminderTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(reminderTime);
            timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(calendar.get(Calendar.MINUTE));
        } catch (Exception e) {
            Log.e(TAG, "Failed to populate fields: " + e.getMessage());
            Toast.makeText(this, "加载备忘录失败，请检查数据完整性", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveMemo() {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();


        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "标题不能为空", Toast.LENGTH_SHORT).show();
            return;
        }


        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String reminderTimeString = sdf.format(calendar.getTime());

        MemoDao memoDao = new MemoDao(this);

        if (isEditMode) {
            // Update existing memo
            memoToEdit.setTitle(title);
            memoToEdit.setContent(content);
            memoToEdit.setCategory(category);
            memoToEdit.setReminderTime(reminderTimeString);

            int result = memoDao.updateMemo(memoToEdit);
            handleResult(result, "编辑成功", "编辑失败");
        } else {
            // Add new memo
            Memo newMemo = new Memo(title, content, category, reminderTimeString);
            long result = memoDao.insertMemo(newMemo);
            handleResult(result, "添加成功", "添加失败");
        }

        finish();
    }


    private void handleResult(long result, String successMessage, String failureMessage) {
        if (result > 0) {
            Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
        } else {
            Toast.makeText(this, failureMessage + ", 请检查输入的数据或网络", Toast.LENGTH_SHORT).show();
        }
    }
}
