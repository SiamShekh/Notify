package com.siam.notify.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.siam.notify.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Help extends Fragment {
    ListView listFAQ;
    HashMap<String, String> hashMap = new HashMap<>();
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        listFAQ = view.findViewById(R.id.listFAQ);

        faq();

        FAQAdapter faqAdapter = new FAQAdapter();
        listFAQ.setAdapter(faqAdapter);

        return view;
    }

    private void faq() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("title", "How do I create a new note?");
        hashMap.put("faq", "To create a new note, tap the '+' button at the bottom-right corner of the screen. A blank note will appear where you can add your text and save it by pressing the save icon.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "How can I delete a note?");
        hashMap.put("faq", "To delete a note, tap the trash can icon next to the note you wish to remove. A confirmation message will appear to ensure you want to delete it permanently.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "How do I search for a specific note?");
        hashMap.put("faq", "You can use the search bar at the top of the Notes screen. Simply type a keyword or phrase, and the app will filter notes containing your search terms.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Can I recover a deleted note?");
        hashMap.put("faq", "Currently, deleted notes cannot be recovered. Please make sure you only delete notes that you no longer need.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "How can I organize my notes?");
        hashMap.put("faq", "Notes can be organized by adding labels, colors, or using the sorting options in the settings. You can also pin important notes to the top for easy access.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Does the app automatically save my notes?");
        hashMap.put("faq", "Yes, your notes are automatically saved as you type, so you don’t have to worry about losing your work.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "How can I change the theme of the app?");
        hashMap.put("faq", "To change the theme, go to the 'Me' tab, tap on 'Settings,' and choose from the available themes to personalize your experience.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Can I sync my notes across multiple devices?");
        hashMap.put("faq", "We currently support syncing via cloud services. Simply sign in with your account in the 'Me' tab to enable synchronization across devices.");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Is there a way to lock my notes for privacy?");
        hashMap.put("faq", "You can enable password protection for individual notes. Go to the note you want to protect, tap the options icon, and select 'Lock Note.'");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "How do I contact customer support?");
        hashMap.put("faq", "If you encounter any issues or need help, you can contact us via the Help tab by filling out the 'Contact Support' form or emailing us at support@yourapp.com.");
        arrayList.add(hashMap);
    }

    protected  class FAQAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.item_faq, parent, false);
            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textFaq = view.findViewById(R.id.textFaq);
            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout faq_item = view.findViewById(R.id.faq_item);
            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView faq_title = view.findViewById(R.id.faq_title);
            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView image_icon = view.findViewById(R.id.image_icon);

            hashMap = arrayList.get(position);
            faq_title.setText(hashMap.get("title"));
            textFaq.setText(hashMap.get("faq"));

            faq_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    image_icon.setImageResource(R.drawable.icon_arrow_top);
                    textFaq.setVisibility(View.VISIBLE);
                }
            });

            return view;
        }
    }
}