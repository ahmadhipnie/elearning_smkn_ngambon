package com.example.elearning_smkn_ngambon.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.elearning_smkn_ngambon.BuildConfig;
import com.example.elearning_smkn_ngambon.R;
import com.example.elearning_smkn_ngambon.adapter.ChatAdapter;
import com.example.elearning_smkn_ngambon.api.apiconfig;
import com.example.elearning_smkn_ngambon.model.ChatModel;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private ImageButton btnSend;
    private EditText etMessage;
    TextView welcomeTextView;
    private RecyclerView rvChat;
    private ChatAdapter chatAdapter;
    private List<ChatModel> chatList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        btnSend = view.findViewById(R.id.btn_send);
        etMessage = view.findViewById(R.id.et_message);
        rvChat = view.findViewById(R.id.recycler_view);
        welcomeTextView = view.findViewById(R.id.welcome_text);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                welcomeTextView.setVisibility(View.GONE);
                buttonCallGeminiAPI(v);
            }
        });

        rvChat = view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setStackFromEnd(true);
        rvChat.setLayoutManager(layoutManager);

        // Deklarasi dan inisialisasi chatList
        chatList = new ArrayList<>();
        // Inisialisasi chatAdapter dengan chatList
        chatAdapter = new ChatAdapter(chatList);
        // Set adapter ke RecyclerView
        rvChat.setAdapter(chatAdapter);

        return view;
    }

    public void buttonCallGeminiAPI(View view) {
        String message = etMessage.getText().toString().trim(); // Mendapatkan pesan dari EditText
        if (!message.isEmpty()) { // Pastikan pesan tidak kosong

            // Tambahkan pesan yang dikirim user
            ChatModel userMessage = new ChatModel(message, false);
            chatList.add(userMessage);

            // Tambahkan pesan "Sedang mengetik"
            ChatModel botTypingMessage = new ChatModel("Sedang mengetik...", true);
            chatList.add(botTypingMessage);

            // Perbarui tampilan RecyclerView menggunakan DiffUtil
            chatAdapter.notifyItemRangeInserted(chatList.size() - 2, 2);

            GenerativeModel gm = new GenerativeModel("gemini-pro", apiconfig.API_KEY);
            GenerativeModelFutures model = GenerativeModelFutures.from(gm);
            Content content = new Content.Builder()
                    .addText(message)
                    .build();

            etMessage.setText(""); // Bersihkan EditText setelah pesan dikirim

            ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
            Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                @Override
                public void onSuccess(GenerateContentResponse result) {
                    String resultText = result.getText();

                    // Hapus pesan "Sedang mengetik" dari chatList
                    chatList.remove(botTypingMessage);

                    // Tambahkan pesan balasan dari bot ke dalam chatList
                    ChatModel botMessage = new ChatModel(resultText, true);
                    chatList.add(botMessage);

                    // Perbarui tampilan RecyclerView menggunakan DiffUtil
                    chatAdapter.notifyItemRangeRemoved(chatList.size() - 2, 2);
                    chatAdapter.notifyItemInserted(chatList.size() - 1);
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                    ChatModel botMessage = new ChatModel(t.toString(), true);
                    ChatModel FailureMessage = new ChatModel("terjadi kesalaahan chat, coba tutup aplikasi lalu buka kembali", true);
                    chatList.add(botMessage);
                    chatList.add(FailureMessage);

                    // Perbarui tampilan RecyclerView menggunakan DiffUtil
                    chatAdapter.notifyItemRangeRemoved(chatList.size() - 2, 2);
                    chatAdapter.notifyItemRangeInserted(chatList.size() - 2, 2);
                }
            }, getActivity().getMainExecutor());
        }
    }
}