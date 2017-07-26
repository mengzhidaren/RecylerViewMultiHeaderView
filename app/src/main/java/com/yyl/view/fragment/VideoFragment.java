package com.yyl.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yyl.view.R;
import com.yyl.view.ui.VideoActivity;

import org.videolan.vlc.VlcVideoView;
import org.videolan.vlc.listener.MediaListenerEvent;

/**
 */
public class VideoFragment extends Fragment {

    private VlcVideoView vlcVideoView;
    private TextView textView;
    String path = "http://img1.peiyinxiu.com/2014121211339c64b7fb09742e2c.mp4";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onStop() {
        super.onStop();
        vlcVideoView.onStop();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoActivity videoActivity = ((VideoActivity) getActivity());
                videoActivity.setVideoFullState(!videoActivity.screenFullVideo);
            }
        });
        vlcVideoView = (VlcVideoView) view.findViewById(R.id.vlcVideo);
        textView = (TextView) view.findViewById(R.id.state);
        vlcVideoView.startPlay(path);
        vlcVideoView.setMediaListenerEvent(new MediaListenerEvent() {
            @Override
            public void eventBuffing(float buffing, boolean show) {
                if (show)
                    textView.setText("buffing=" + buffing);
                else textView.setText("isPlaying");
            }

            @Override
            public void eventPlayInit(boolean openClose) {
                textView.setText("open");
            }

            @Override
            public void eventStop(boolean isPlayError) {
                textView.setText("Stop");
            }

            @Override
            public void eventError(int error, boolean show) {
                textView.setText("Error");
            }

            @Override
            public void eventPlay(boolean isPlaying) {
                textView.setText("isPlaying=" + isPlaying);
            }
        });
    }
}
