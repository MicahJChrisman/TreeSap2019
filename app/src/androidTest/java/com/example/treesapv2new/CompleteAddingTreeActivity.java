package com.example.treesapv2new;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class CompleteAddingTreeActivity {
    @Rule
    public ActivityTestRule<Add_Tree_Activity> add_tree_activityActivityTestRule = new ActivityTestRule<Add_Tree_Activity>(Add_Tree_Activity.class);
    @Rule
    public ActivityTestRule<Tree_Bark_Activity> tree_bark_activityActivityTestRule = new ActivityTestRule<Tree_Bark_Activity>(Tree_Bark_Activity.class);
    @Rule
    public ActivityTestRule<Tree_Leaf_Activity> tree_leaf_activityActivityTestRule = new ActivityTestRule<Tree_Leaf_Activity>(Tree_Leaf_Activity.class);
    @Rule
    public ActivityTestRule<Tree_Pic_Activity> tree_pic_activityActivityTestRule = new ActivityTestRule<Tree_Pic_Activity>(Tree_Pic_Activity.class);
    @Rule
    public ActivityTestRule<Tree_Other_Info_Activity> tree_other_info_activityActivityTestRule = new ActivityTestRule<Tree_Other_Info_Activity>(Tree_Other_Info_Activity.class);

    Instrumentation.ActivityMonitor barkMonitor = getInstrumentation().addMonitor(Tree_Bark_Activity.class.getName(), null, false);

    private Add_Tree_Activity add_tree_activity;
    private Tree_Bark_Activity tree_bark_activity;
    private Tree_Leaf_Activity tree_leaf_activity;
    private Tree_Pic_Activity tree_pic_activity;
    private Tree_Other_Info_Activity tree_other_info_activity;
    @Before
    public void setUp() throws Exception {
        add_tree_activity = add_tree_activityActivityTestRule.getActivity();
        tree_bark_activity = tree_bark_activityActivityTestRule.getActivity();
        tree_leaf_activity = tree_leaf_activityActivityTestRule.getActivity();
        tree_pic_activity = tree_pic_activityActivityTestRule.getActivity();
        tree_other_info_activity = tree_other_info_activityActivityTestRule.getActivity();
    }

    @Test
    public void testAllInput(){
        onView(withId(R.id.get_location_button)).perform(click());
        assertEquals("42.78779833333333", ((TextView)add_tree_activity.findViewById(R.id.lat_putter)).getText().toString());
        assertEquals("-86.10569833333332", ((TextView)add_tree_activity.findViewById(R.id.long_putter)).getText().toString());
        assertEquals(0, add_tree_activity.findViewById(R.id.next_add_tree).getVisibility());
        onView(withId(R.id.next_add_tree)).perform(click());


//        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
//        mMainActivity = getActivity();
//        Activity blah = (Activity) InstrumentationRegistry.getContext();


//            Intent intent = new Intent(InstrumentationRegistry.getContext(),Tree_Bark_Activity.class);
//            intent.putExtra("latitude", ((TextView)add_tree_activity.findViewById(R.id.lat_putter)).getText().toString());
//            intent.putExtra("longitude", ((TextView)add_tree_activity.findViewById(R.id.long_putter)).getText().toString());

        onView(withId(R.id.add_bark_pic)).perform(click());
        assertEquals(View.VISIBLE, tree_bark_activityActivityTestRule.getActivity().findViewById(R.id.camera_appear).getVisibility());

        //onView(withId(R.id.camera_appear)).perform(click());
        assertEquals(View.VISIBLE, tree_bark_activityActivityTestRule.getActivity().findViewById(R.id.next_pic_bark).getVisibility());

//        ByteArrayOutputStream stream =
//        byte[] z = null;
        byte[] z = tree_bark_activityActivityTestRule.getActivity().getByteArray();

//        assertEquals(z, tree_bark_activityActivityTestRule.getActivity().getByteArray());
        onView(withId(R.id.next_pic_bark)).perform(click());
//        while(tree_leaf_activityActivityTestRule.getActivity().findViewById(R.id.next_pic_leaf).getVisibility() == 8) {
//
//        }

        onView(withId(R.id.add_leaf_pic)).perform(click());
        assertEquals(View.VISIBLE, tree_leaf_activityActivityTestRule.getActivity().findViewById(R.id.camera_appear_leaf).getVisibility());
        assertEquals(View.VISIBLE, tree_leaf_activityActivityTestRule.getActivity().findViewById(R.id.next_pic_leaf).getVisibility());
        onView(withId(R.id.next_pic_leaf)).perform(click());

        onView(withId(R.id.add_full_pic)).perform(click());
        assertEquals(View.VISIBLE, tree_pic_activityActivityTestRule.getActivity().findViewById(R.id.camera_appear_full).getVisibility());
        assertEquals(View.VISIBLE, tree_pic_activityActivityTestRule.getActivity().findViewById(R.id.next_pic_full).getVisibility());
        onView(withId(R.id.next_pic_full)).perform(click());

        assertEquals("42.78779833333333", ((TextView)tree_other_info_activityActivityTestRule.getActivity().findViewById(R.id.lat_thing)).getText().toString());
        assertEquals("-86.10569833333332", ((TextView)tree_other_info_activityActivityTestRule.getActivity().findViewById(R.id.long_thing)).getText().toString());
        //assertEquals(z,((ImageView)tree_other_info_activityActivityTestRule.getActivity().findViewById(R.id.show_bark_pic))).toByteArray();


        ImageView imageView = tree_other_info_activityActivityTestRule.getActivity().findViewById(R.id.show_bark_pic);
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageInbyte = byteArrayOutputStream.toByteArray();

        ImageView imageView2 = tree_other_info_activityActivityTestRule.getActivity().findViewById(R.id.show_leaf_pic);
        Bitmap bitmap2 = ((BitmapDrawable) imageView2.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
        bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream2);
        byte[] imageInbyte2 = byteArrayOutputStream.toByteArray();

        boolean x = Arrays.equals(imageInbyte,imageInbyte2);

        ImageView imageView3 = tree_other_info_activityActivityTestRule.getActivity().findViewById(R.id.show_leaf_pic);
        Bitmap bitmap3 = ((BitmapDrawable) imageView3.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream3 = new ByteArrayOutputStream();
        bitmap3.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream3);
        byte[] imageInbyte3 = byteArrayOutputStream.toByteArray();

        boolean y = Arrays.equals(imageInbyte,imageInbyte3);

        assertEquals(true, x);
        assertEquals(true, y);
    }

}
