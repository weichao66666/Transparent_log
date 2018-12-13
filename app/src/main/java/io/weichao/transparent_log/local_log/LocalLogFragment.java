package io.weichao.transparent_log.local_log;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.weichao.transparent_log.R;
import io.weichao.transparent_log.bean.LocalLogBean;
import io.weichao.transparent_log.util.HardwareInfoUtil;
import io.weichao.transparent_log.widget.DraggableFloatingActionButton;

public class LocalLogFragment extends Fragment implements LocalLogFragmentView {
    private static final String TAG = "LocalLogFragment";

    private View mRootView;

    private RecyclerViewAdapter mRecyclerViewAdapter;

    public static LocalLogFragment newInstance() {
        return new LocalLogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_log, null);

        final Point displayPoint = HardwareInfoUtil.getDisplayPoint(getContext());
        final int scrollY = displayPoint.y >> 1;

        final RecyclerView recyclerView = mRootView.findViewById(R.id.recyclerview);
        ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);// 取消动画，否则 notify 会闪一下
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewAdapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(mRecyclerViewAdapter);

        final DraggableFloatingActionButton exitBtn = mRootView.findViewById(R.id.btn_exit);
        exitBtn.setListener(new DraggableFloatingActionButton.Listener() {
            @Override
            public void onClick() {
                hide();
            }
        });
        final DraggableFloatingActionButton previousPageBtn = mRootView.findViewById(R.id.btn_previous_page);
        previousPageBtn.setListener(new DraggableFloatingActionButton.Listener() {
            @Override
            public void onClick() {
                recyclerView.smoothScrollBy(0, -scrollY);
            }
        });
        final DraggableFloatingActionButton nextPageBtn = mRootView.findViewById(R.id.btn_next_page);
        nextPageBtn.setListener(new DraggableFloatingActionButton.Listener() {
            @Override
            public void onClick() {
                recyclerView.smoothScrollBy(0, scrollY);
            }
        });
        final DraggableFloatingActionButton clearBtn = mRootView.findViewById(R.id.btn_clear);
        clearBtn.setListener(new DraggableFloatingActionButton.Listener() {
            @Override
            public void onClick() {
                clear();
            }
        });

        return mRootView;
    }

    @Override
    public void hide() {
        mRootView.setVisibility(View.GONE);
    }

    @Override
    public void show() {
        mRootView.setVisibility(View.VISIBLE);
    }

    @Override
    public void clear() {
        mRecyclerViewAdapter.mList.clear();
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void log(int level, String msg) {
        mRecyclerViewAdapter.mList.add(new LocalLogBean(level, msg));
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
        private ArrayList<LocalLogBean> mList;

        private RecyclerViewAdapter() {
            mList = new ArrayList<>();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_log_holder, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final LocalLogBean bean = mList.get(position);
            holder.textview.setText(bean.getMsg());
            switch (bean.getLevel()) {
                case 4:
                    holder.textview.setTextColor(Color.RED);
                    break;
                default:
                    holder.textview.setTextColor(Color.WHITE);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return mList != null ? mList.size() : 0;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView textview;

            private MyViewHolder(View view) {
                super(view);
                textview = view.findViewById(R.id.textview);
            }
        }
    }
}