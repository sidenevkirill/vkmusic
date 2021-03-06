package com.sidenev.plus.vkmusic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.root.vkcoffee.R;
import com.sidenev.plus.vkmusic.jsplayer.JcPlayerExceptions.JcAudio;
import com.sidenev.plus.vkmusic.jsplayer.JcPlayerExceptions.OnLoadMoreListener;

import java.util.List;


public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioAdapterViewHolder> {
    public static final String TAG = AudioAdapter.class.getSimpleName();

    private List<JcAudio> jcAudioList;
    private SparseArray<Float> progressMap = new SparseArray<>();

    private static OnItemClickListener mListener ;
    private static View.OnClickListener mDownload;
    public MainActivity activity;

    // Define the mListener interface
    public interface OnItemClickListener {
        void onItemClick(int position);

        void onSongItemDeleteClicked(int position);
    }
    public interface OnClickListener {
        void onClickDownload(View v);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void setOnClickDownload(View.OnClickListener listener) {
        this.mDownload = listener;
    }

    public AudioAdapter(List<JcAudio> jcAudioList, Context context) {
        this.jcAudioList = jcAudioList;
        activity = (MainActivity) context;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return jcAudioList.get(position).getId();
    }

    @Override
    public AudioAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_item, parent, false);
        return new AudioAdapterViewHolder(view);
//        audiosViewHolder.itemView.setOnClickListener(this);
//        return audiosViewHolder;
    }

    public void setItems(List<JcAudio> list){
        jcAudioList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(AudioAdapterViewHolder holder, final int position) {
        String title = null;
        String name = null;
        try {
            title = jcAudioList.get(position).getTitle().split("--")[1];
            name = jcAudioList.get(position).getTitle().split("--")[0];
        } catch (Exception e) {
            title = jcAudioList.get(position).getTitle().split("-")[1];
            name = jcAudioList.get(position).getTitle().split("-")[0];
            e.printStackTrace();
        }
        holder.audioTitle.setText(title);
        holder.audioName.setText(" "+name);
        holder.itemView.setTag(jcAudioList.get(position));
        holder.imgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startDownload(jcAudioList.get(position).getTitle(),
                        jcAudioList.get(position).getPath(),"");
            }
        });
        try{
            if(jcAudioList.get(position).getAlbum_img().length()>10)Glide.with(holder.imgAlbum.getContext())
                    .load(jcAudioList.get(position).getAlbum_img())
                    .into(holder.imgAlbum);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //if(activity.player.getCurrentAudio().getPath())
        applyProgressPercentage(holder, progressMap.get(position, 0.0f));
    }

    /**
     * Applying percentage to progress.
     * @param holder ViewHolder
     * @param percentage in float value. where 1 is equals as 100%
     */
    private void applyProgressPercentage(AudioAdapterViewHolder holder, float percentage) {
        //holder.audioTitle.setVisibility(View.GONE);
        Log.d(TAG, "applyProgressPercentage() with percentage = " + percentage);
        LinearLayout.LayoutParams progress = (LinearLayout.LayoutParams) holder.viewProgress.getLayoutParams();
        LinearLayout.LayoutParams antiProgress = (LinearLayout.LayoutParams) holder.viewAntiProgress.getLayoutParams();

        progress.weight = percentage;
        holder.viewProgress.setLayoutParams(progress);

        antiProgress.weight = 1.0f - percentage;
        holder.viewAntiProgress.setLayoutParams(antiProgress);
    }
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;


    @Override
    public int getItemCount() {
        return jcAudioList == null ? 0 : jcAudioList.size();
    }

    public void updateProgress(JcAudio jcAudio, float progress) {
        int position = jcAudioList.indexOf(jcAudio);
        Log.d(TAG, "Progress = " + progress);


        progressMap.put(position, progress);
        if(progressMap.size() > 1) {
            for(int i = 0; i < progressMap.size(); i++) {
                if(progressMap.keyAt(i) != position) {
                    Log.d(TAG, "KeyAt(" + i + ") = " + progressMap.keyAt(i));
                    notifyItemChanged(progressMap.keyAt(i));
                    progressMap.delete(progressMap.keyAt(i));
                }
            }
        }
        notifyItemChanged(position);
    }

    static class AudioAdapterViewHolder extends RecyclerView.ViewHolder{
        private TextView audioTitle;
        private Button btnDelete;
        private View viewProgress;
        private View viewAntiProgress;
        private TextView audioName;
        private ImageView imgDownload, imgAlbum;

        public AudioAdapterViewHolder(View view){
            super(view);
            this.audioTitle = (TextView) view.findViewById(R.id.audio_title);
            this.audioName = (TextView) view.findViewById(R.id.audio_name);
            this.btnDelete = (Button) view.findViewById(R.id.btn_delete);
            this.imgDownload = (ImageView) view.findViewById(R.id.audio_download);
            this.imgAlbum = (ImageView) view.findViewById(R.id.audio_album);
            viewProgress = view.findViewById(R.id.song_progress_view);
            viewAntiProgress = view.findViewById(R.id.song_anti_progress_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (mListener != null) mListener.onItemClick(getAdapterPosition());
                }
            });


        }
    }
}
