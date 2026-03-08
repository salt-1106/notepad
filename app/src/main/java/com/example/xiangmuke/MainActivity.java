package com.example.xiangmuke;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.example.xiangmuke.R;
import com.example.xiangmuke.adapters.MemoAdapter;
import com.example.xiangmuke.database.MemoDao;
import com.example.xiangmuke.models.Memo;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private ListView listView;
    private Button addButton;
    private Button deleteButton;
    private Button editButton;
    private Button searchButton;
    private Button countButton;
    private MemoDao memoDao;
    private List<Memo> currentMemoList;
    private MemoAdapter adapter;
    private int positionId = AdapterView.INVALID_POSITION; // 默认无效

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        memoDao = new MemoDao(this);

        calendarView = findViewById(R.id.calendarView);
        listView = findViewById(R.id.listView);
        addButton = findViewById(R.id.add_button);
        deleteButton = findViewById(R.id.delete_button);
        editButton = findViewById(R.id.edit_button);
        searchButton = findViewById(R.id.search_button);
        countButton = findViewById(R.id.count_button);

        // 日历点击事件
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Log.d("MainActivity", "Selected date: " + year + "-" + (month + 1) + "-" + dayOfMonth);
            Toast.makeText(MainActivity.this, "Selected date: " + year + "-" + (month + 1) + "-" + dayOfMonth, Toast.LENGTH_SHORT).show();
        });

        // 添加按钮
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditMemoActivity.class);
            startActivityForResult(intent, 1);  // 使用 startActivityForResult 以便更新列表
        });

        // 删除按钮
        deleteButton.setOnClickListener(v -> {
            if (positionId != AdapterView.INVALID_POSITION) {
                Memo memoToDelete = currentMemoList.get(positionId);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("确认删除")
                        .setMessage("确定要删除备忘录 '" + memoToDelete.getTitle() + "' 吗？")
                        .setPositiveButton("确定", (dialog, which) -> {
                            int result = memoDao.deleteMemoById(memoToDelete.getId());
                            if (result > 0) {
                                Toast.makeText(MainActivity.this, "备忘录删除成功", Toast.LENGTH_SHORT).show();
                                loadAndShowMemos();
                            } else {
                                Toast.makeText(MainActivity.this, "删除失败，请稍后重试", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            } else {
                Toast.makeText(MainActivity.this, "请先选择要删除的备忘录", Toast.LENGTH_SHORT).show();
            }
        });

        // 编辑按钮
        editButton.setOnClickListener(v -> {
            if (positionId != AdapterView.INVALID_POSITION) {
                Memo memoToEdit = currentMemoList.get(positionId);
                Intent intent = new Intent(MainActivity.this, AddEditMemoActivity.class);
                intent.putExtra("memo", memoToEdit);
                startActivityForResult(intent, 1);
            } else {
                Toast.makeText(MainActivity.this, "请先选择要编辑的备忘录", Toast.LENGTH_SHORT).show();
            }
        });

        // 查询按钮
        searchButton.setOnClickListener(v -> {
            final AppCompatEditText input = new AppCompatEditText(MainActivity.this);
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("请输入标题关键词")
                    .setView(input)
                    .setPositiveButton("查询", (dialog, which) -> {
                        String keyword = input.getText().toString().trim();
                        if (!keyword.isEmpty()) {
                            List<Memo> searchedMemos = memoDao.searchMemosByTitle(keyword);
                            if (searchedMemos.size() > 0) {
                                currentMemoList = searchedMemos;
                                adapter = new MemoAdapter(MainActivity.this, currentMemoList);
                                listView.setAdapter(adapter);
                            } else {
                                Toast.makeText(MainActivity.this, "未找到相关备忘录", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "请输入有效的关键词", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        });

        // 统计按钮
        countButton.setOnClickListener(v -> {
            int count = memoDao.countAllMemos();
            Toast.makeText(MainActivity.this, "备忘录总数：" + count, Toast.LENGTH_SHORT).show();
        });

        // 加载并显示所有备忘录
        loadAndShowMemos();

        // 设置列表项点击监听
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (positionId == position) {
                listView.setItemChecked(position, false);
                positionId = AdapterView.INVALID_POSITION;
            } else {
                if (positionId != AdapterView.INVALID_POSITION) {
                    listView.setItemChecked(positionId, false);  // 取消之前的选中
                }
                listView.setItemChecked(position, true);
                positionId = position;
            }
        });

        // 长按取消选中
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            listView.setItemChecked(position, false);
            positionId = AdapterView.INVALID_POSITION;
            return true;
        });
    }

    private void loadAndShowMemos() {
        currentMemoList = memoDao.getAllMemos();
        adapter = new MemoAdapter(this, currentMemoList);
        listView.setAdapter(adapter);
        positionId = AdapterView.INVALID_POSITION; // 重置选中的项
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // 编辑或添加备忘录后，重新加载并展示备忘录列表
            loadAndShowMemos();
        }
    }
}
