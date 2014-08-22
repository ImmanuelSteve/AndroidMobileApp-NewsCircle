package id.ac.ukdw.newscircle;

/**
 * Created by Steven Renaldo Antony / 71110054 on 18/04/14.
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MobileArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String [] news;
    private final String [] date;
    private final String[] publisher;

    public MobileArrayAdapter(Context context, String[] values, String[] values1,String[] values2) {
        super(context, R.layout.list_activity_main,values);
        this.context = context;
        this.news = values;
        this.date = values1;
        this.publisher = values2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_activity_main, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        TextView textView1 = (TextView) rowView.findViewById(R.id.label1);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
        ImageView imageView1 = (ImageView) rowView.findViewById(R.id.arrow);
        textView.setText(news[position]);
        textView1.setText(date[position]);

        // Change icon based on name
        String s = publisher[position];

        System.out.println(s);

        if (s.equals("")){
        	imageView.setImageResource(R.drawable.icon_notice);
        	imageView1.setImageResource(R.drawable.blank);
        }else if(s.equals("Okezone")) {
            imageView.setImageResource(R.drawable.okezone);
            imageView1.setImageResource(R.drawable.arrow);
        }else if(s.equals("Tribunnews")) {
            imageView.setImageResource(R.drawable.tribunnews);
            imageView1.setImageResource(R.drawable.arrow);
        }else if(s.equals("Harian Analisa (Blog)")) {
            imageView.setImageResource(R.drawable.analisa_logo);
            imageView1.setImageResource(R.drawable.arrow);
        }else if(s.equals("KOMPAS.com")) {
            imageView.setImageResource(R.drawable.kompas);
            imageView1.setImageResource(R.drawable.arrow);
        }else if(s.equals("Tempo.co")) {
            imageView.setImageResource(R.drawable.tempoco);
            imageView1.setImageResource(R.drawable.arrow);
        }else if(s.equals("VIVA.co.id")) {
            imageView.setImageResource(R.drawable.viva);
            imageView1.setImageResource(R.drawable.arrow);
        }else if(s.equals("merdeka.com")) {
            imageView.setImageResource(R.drawable.merdeka);
            imageView1.setImageResource(R.drawable.arrow);
        }else if(s.equals("ANTARA")) {
            imageView.setImageResource(R.drawable.antaranews);
            imageView1.setImageResource(R.drawable.arrow);
        }else if(s.equals("Detikcom")) {
            imageView.setImageResource(R.drawable.detik);
            imageView1.setImageResource(R.drawable.arrow);
        }else if(s.equals("Suara Merdeka CyberNews")) {
            imageView.setImageResource(R.drawable.suaramerdeka);
            imageView1.setImageResource(R.drawable.arrow);
        }else if(s.equals("BeritaSatu")) {
            imageView.setImageResource(R.drawable.beritasatu);
            imageView1.setImageResource(R.drawable.arrow);
        }else if(s.equals("VOA Indonesia")) {
            imageView.setImageResource(R.drawable.voaindonesia);
            imageView1.setImageResource(R.drawable.arrow);
        }else if(s.equals("Pikiran Rakyat")) {
            imageView.setImageResource(R.drawable.pikiranrakyat);
            imageView1.setImageResource(R.drawable.arrow);
        }else if(s.equals("Tribunjogja.com")) {
            imageView.setImageResource(R.drawable.tribunjogja);
            imageView1.setImageResource(R.drawable.arrow);
        }else if(s.equals("Jakarta Post")) {
            imageView.setImageResource(R.drawable.jakartapost);
            imageView1.setImageResource(R.drawable.arrow);
        }else if(s.equals("The Globe Journal")) {
            imageView.setImageResource(R.drawable.the_globe_journal);
            imageView1.setImageResource(R.drawable.arrow);
        }else {
            imageView.setImageResource(R.drawable.logo);
            imageView1.setImageResource(R.drawable.arrow);
        }

        return rowView;
    }
}