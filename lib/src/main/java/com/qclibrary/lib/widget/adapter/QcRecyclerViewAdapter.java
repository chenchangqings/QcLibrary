package com.qclibrary.lib.widget.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class QcRecyclerViewAdapter<T> extends RecyclerView.Adapter<QcRecyclerViewAdapter.BaseViewHolder> {
    protected List<T> mData = new ArrayList<>();
    protected Context mContext;
    private Map<Integer, HolderType> layouts = new HashMap<>();
    private OnLoadMoreListener mLoadMoreListener;
    private boolean mLoadMoreEnable;
    private boolean mLoading = false;
    private boolean mLoadMoreNoData = false;
    public  static final int TYPE_LOAD_MORE = 0x00000222;
    private RecyclerView mRecyclerView;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        mContext = recyclerView.getContext();
    }

    @NonNull
    @Override
    public QcRecyclerViewAdapter.BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        mRecyclerView = (RecyclerView) viewGroup;
        mContext = viewGroup.getContext();
        View rootView = LayoutInflater.from(mContext).inflate(getLayoutId(viewType), viewGroup, false);
        return createGenericKInstHance(getHolderType(viewType), rootView);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getLoadMoreViewPosition()) {
            if (getLoadMoreViewCount() == 1) {
                return TYPE_LOAD_MORE;
            } else {
                return onItemViewType(position);
            }
        } else {
            return onItemViewType(position);
        }
    }

    public int onItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private QcRecyclerViewAdapter.BaseViewHolder createGenericKInstHance(Class z, View view) {
        try {
            Constructor constructor;
            // inner and unstatic class
            if (z.isMemberClass() && !Modifier.isStatic(z.getModifiers())) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                constructor.setAccessible(true);
                return (QcRecyclerViewAdapter.BaseViewHolder) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                return (QcRecyclerViewAdapter.BaseViewHolder) constructor.newInstance(view);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull QcRecyclerViewAdapter.BaseViewHolder viewHolder, int position) {
        autoLoadMore(position);
        viewHolder.setData(position);
    }

    public void setNewData(@Nullable List<T> data) {
        this.mData = data == null ? new ArrayList<>() : data;
        if (mLoadMoreListener != null) {
            mLoadMoreEnable = true;
            mLoading = false;
        }
        notifyDataSetChanged();
    }


    public void addData(@NonNull T data) {
        mData.add(data);
        notifyItemInserted(mData.size());
        compatibilityDataSizeChanged(1);
    }

    public void addArray(@NonNull Collection newData) {
        mData.addAll(newData);
        notifyDataSetChanged();
    }


    public List<T> getData() {
        return mData;
    }

    public int getLastPosition() {
        return mData == null ? 0 : mData.size() - 1;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : (mData.size() + getLoadMoreViewCount());
    }


    private int getLayoutId(int viewType) {
        return layouts.get(viewType).getLayoutId();
    }

    private Class getHolderType(int viewType) {
        return layouts.get(viewType).getHolderType();
    }

    protected void addItemType(int type, @LayoutRes int layoutResId, Class holderClass) {
        layouts.put(type, new HolderType(layoutResId, holderClass));
    }

    /**
     * compatible getLoadMoreViewCount and getEmptyViewCount may change
     *
     * @param size Need compatible data size
     */
    protected void compatibilityDataSizeChanged(int size) {
        final int dataSize = mData == null ? 0 : mData.size();
        if (dataSize == size) {
            notifyDataSetChanged();
        }
    }

    protected void autoLoadMore(int position) {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        if (position < getItemCount() - 1) {
            return;
        }

        if (!mLoading) {
            mLoading = true;
            if (mRecyclerView != null) {
                mRecyclerView.post(() -> mLoadMoreListener.onLoadMore());
            } else {
                mLoadMoreListener.onLoadMore();
            }
        }
    }

    public boolean isLoading() {
        return mLoading;
    }

    public void notifySetEnableLoadMore(boolean enable) {
        int oldLoadMoreCount = getLoadMoreViewCount();
        mLoadMoreEnable = enable;
        int newLoadMoreCount = getLoadMoreViewCount();

        if (oldLoadMoreCount == 1) {
            if (newLoadMoreCount == 0) {
                notifyItemRemoved(getLoadMoreViewPosition());
            }
        } else {
            if (newLoadMoreCount == 1) {
                notifyItemInserted(getLoadMoreViewPosition());
            }
        }
    }


    public void setEnableLoadMore(boolean enable) {
        mLoadMoreEnable = enable;
    }

    public void loadMoreComplete() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        notifyItemChanged(getLoadMoreViewPosition());
    }

    public int getLoadMoreViewPosition() {
        return mData.size();
    }

    public int getLoadMoreViewCount() {
        if (mLoadMoreListener == null || !mLoadMoreEnable) {
            return 0;
        }

        if (layouts.get(TYPE_LOAD_MORE) == null) {
            return 0;
        }

        if (!mLoadMoreNoData && mData.size() == 0) {
            return 0;
        }
        return 1;
    }

    public void setLoadMoreNoData(boolean mLoadMoreNoData) {
        this.mLoadMoreNoData = mLoadMoreNoData;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        openLoadMore(loadMoreListener);
    }

    private void openLoadMore(OnLoadMoreListener loadMoreListener) {
        this.mLoadMoreListener = loadMoreListener;
        mLoadMoreEnable = true;
        mLoading = false;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public Context getContext() {
        return mContext;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public abstract static class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public abstract void setData(int position);
    }

    public class HolderType {
        private int layoutId;
        private Class holderType;

        public HolderType(int layoutId, Class holderType) {
            this.layoutId = layoutId;
            this.holderType = holderType;
        }

        public int getLayoutId() {
            return layoutId;
        }

        public void setLayoutId(int layoutId) {
            this.layoutId = layoutId;
        }

        public Class getHolderType() {
            return holderType;
        }

        public void setHolderType(Class holderType) {
            this.holderType = holderType;
        }
    }


}
