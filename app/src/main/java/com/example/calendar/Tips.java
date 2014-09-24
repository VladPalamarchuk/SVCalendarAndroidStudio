package com.example.calendar;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.sliders.SlideOutRightAnimator;

public final class Tips {
    public static View view;

    public static class ChangeCalendarView implements View.OnClickListener {

        private View skip;

        // private View next;
        private OpenMenuActions openMenuActions;

        public ChangeCalendarView(Context context) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.tip_change_calendar, null);

            try {

                ((TextView) view.findViewById(R.id.tip2_text1))
                        .setText(MainActivity.getInstance().language.TIP2_TEXT1);
                ((TextView) view.findViewById(R.id.tip2_text2))
                        .setText(MainActivity.getInstance().language.TIP2_TEXT2);

            } catch (Exception e) {
                Log.e(getClass().toString(), "e = " + e);
            }

            skip = view.findViewById(R.id.tip_open_menu_skip);
            // next = view.findViewById(R.id.tip_open_menu_next);

            skip.setOnClickListener(this);
            // next.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);

            if (openMenuActions != null) {
                switch (v.getId()) {
                    case R.id.tip_open_menu_skip:
                        openMenuActions.skip();
                        break;
                    // case R.id.tip_open_menu_next:
                    // openMenuActions.next();
                    // break;
                }
            }
        }

        public void setOpenMenuActions(OpenMenuActions openMenuActions) {
            this.openMenuActions = openMenuActions;
        }

        public View getView() {
            return view;
        }
    }

    public static class OpenMenu implements View.OnClickListener {

        private View view;
        private View finger;
        private View skip;
        // private View next;

        private OpenMenuActions openMenuActions;

        public OpenMenu(Context context) {

            LayoutInflater inflater = (LayoutInflater) context

                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.tip_open_menu, null);

            try {

                ((TextView) view.findViewById(R.id.tip1_text1))
                        .setText(MainActivity.getInstance().language.TIP1_TEXT1);
                ((TextView) view.findViewById(R.id.tip1_text2))
                        .setText(MainActivity.getInstance().language.TIP1_TEXT2);
            } catch (Exception e) {
            }

            finger = view.findViewById(R.id.tip_finger);
            skip = view.findViewById(R.id.tip_open_menu_skip);
            // next = view.findViewById(R.id.tip_open_menu_next);

            skip.setOnClickListener(this);
            // next.setOnClickListener(this);


        }

        public View getView() {
            return view;
        }

        public void start() {
            SlideOutRightAnimator animator = new SlideOutRightAnimator();
            animator.setDuration(3000);
            animator.setStartDelay(500);
            animator.animate(finger);
        }

        public void setOpenMenuActions(OpenMenuActions openMenuActions) {
            this.openMenuActions = openMenuActions;
        }

        @Override
        public void onClick(View v) {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);

            if (openMenuActions != null) {
                switch (v.getId()) {
                    case R.id.tip_open_menu_skip:
                        openMenuActions.skip();
                        break;
                    // case R.id.tip_open_menu_next:
                    // openMenuActions.next();
                    // break;
                }
            }
        }

    }

    public static class CreateEvent implements View.OnClickListener {

        private Context context;

        private View view;
        private View skip;
        // private View next;

        private OpenMenuActions openMenuActions;

        public CreateEvent(Context context) {
            this.context = context;

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.tip_create_event, null);

            try {

                ((TextView) view.findViewById(R.id.tip3_text1))
                        .setText(MainActivity.getInstance().language.TIP3_TEXT1);
                ((TextView) view.findViewById(R.id.tip3_text2))
                        .setText(MainActivity.getInstance().language.TIP3_TEXT2);

            } catch (Exception e) {
                Log.e(getClass().toString(), "e = " + e);
            }

            skip = view.findViewById(R.id.tip_open_menu_skip);
            // next = view.findViewById(R.id.tip_open_menu_next);

            // skip.setOnClickListener(this);
            // next.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);

            if (openMenuActions != null) {
                switch (v.getId()) {
                    case R.id.tip_open_menu_skip:
                        openMenuActions.skip();
                        break;
                    // case R.id.tip_open_menu_next:
                    // openMenuActions.next();
                    // break;
                }
            }
        }

        public void setOpenMenuActions(OpenMenuActions openMenuActions) {
            this.openMenuActions = openMenuActions;
        }

        public View getView() {
            return view;
        }
    }

    public static interface OpenMenuActions {
        public void skip();

        public void next();
    }

}
