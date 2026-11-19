/*
 * Copyright 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.material.search;

import com.google.android.material.test.R;

import static com.google.common.truth.Truth.assertThat;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import com.google.android.material.appbar.AppBarLayout;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

/** Tests for the lift on scroll layout of {@link SearchBar} shown in docs/components/Search.md. */
@RunWith(RobolectricTestRunner.class)
public class SearchBarLiftOnScrollTest {

  private static final int WIDTH = 1080;
  private static final int HEIGHT = 1920;

  @Test
  public void liftOnScroll_withDefaultScrollFlagsDisabled_scrollingViewFitsBelowAppBar() {
    View root = inflateAndLayout(R.layout.test_search_bar_lift_on_scroll);
    AppBarLayout appBarLayout = root.findViewById(R.id.app_bar_layout);
    View scrollingView = root.findViewById(R.id.scrolling_view);

    assertThat(appBarLayout.getTotalScrollRange()).isEqualTo(0);
    assertThat(scrollingView.getTop()).isEqualTo(appBarLayout.getBottom());
    assertThat(scrollingView.getBottom()).isEqualTo(HEIGHT);
  }

  @Test
  public void liftOnScroll_withDefaultScrollFlags_searchBarScrollsAway() {
    View root = inflateAndLayout(R.layout.test_search_bar_lift_on_scroll_default_scroll_flags);
    AppBarLayout appBarLayout = root.findViewById(R.id.app_bar_layout);
    View scrollingView = root.findViewById(R.id.scrolling_view);

    // SearchBar applies its default scroll flags, so the app bar scrolls away and the scrolling
    // view is measured taller than the space left below the app bar.
    assertThat(appBarLayout.getTotalScrollRange()).isGreaterThan(0);
    assertThat(scrollingView.getBottom()).isGreaterThan(HEIGHT);
  }

  private static View inflateAndLayout(int layoutResId) {
    AppCompatActivity activity = Robolectric.buildActivity(TestActivity.class).setup().get();
    activity.setContentView(layoutResId);
    View root = activity.findViewById(R.id.scrolling_view).getRootView();
    View content = (View) activity.findViewById(R.id.app_bar_layout).getParent();
    content.measure(
        View.MeasureSpec.makeMeasureSpec(WIDTH, View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(HEIGHT, View.MeasureSpec.EXACTLY));
    content.layout(0, 0, WIDTH, HEIGHT);
    return root;
  }

  private static class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle bundle) {
      super.onCreate(bundle);
      setTheme(R.style.Theme_Material3_Light);
    }
  }
}
