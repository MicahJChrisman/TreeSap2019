package com.example.treesapv2new;

import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class Tree_Bark_ActivityTest {

    @Rule
    public ActivityTestRule<Tree_Bark_Activity> mActivityTestRule = new ActivityTestRule<Tree_Bark_Activity>(Tree_Bark_Activity.class);

    private Tree_Bark_Activity mActivity;

    Instrumentation.ActivityMonitor nextMonitor = getInstrumentation().addMonitor(Tree_Leaf_Activity.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

    @Test
    public void onCreate() {
        TextView exit = mActivity.findViewById(R.id.bark_pic_close);
        assertNotNull(exit);
        TextView title = mActivity.findViewById(R.id.bark_pic_header);
        assertNotNull(title);
        TextView skip = mActivity.findViewById(R.id.skip_bark_tree);
        assertNotNull(skip);
        Button next = mActivity.findViewById(R.id.next_pic_bark);
        assertNotNull(next);
        assertEquals(8, next.getVisibility());
        ImageButton addPic = mActivity.findViewById(R.id.add_bark_pic);
        assertNotNull(addPic);
        TextView instructions = mActivity.findViewById(R.id.bark_text);
        assertNotNull(instructions);
        ImageView cameraAppear = mActivity.findViewById(R.id.camera_appear);
        assertNotNull(cameraAppear);
        assertEquals(8, cameraAppear.getVisibility());
    }

    @Test
    public void cameraButtonPressed(){
        onView(withId(R.id.add_bark_pic)).perform(click());
        assertEquals(2, mActivity.findViewById(R.id.camera_appear).getVisibility());
        onView(isRoot()).perform(pressBack());
        //TODO At the moment the user can just leave the camera view w/o taking a picture, and then they can press next,
        // so they never get a message about verifying the tree.
        // Should there be a message asking if they are sure they want to leave the camera w/o taking a picture?

    }

//    @Test
//    public void onActivityResult() {
//    }
}