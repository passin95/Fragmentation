package me.passin.fragmentation;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import java.util.Collections;
import java.util.List;

/**
 * @author: passin
 * @date: 2019/6/30 12:24
 */
public class Fragmentation {

    private static final String ARGS_CONTAINER_ID = "args_container_id";

    private final FragmentManager mFragmentManager;
    private final FragmentTransaction mFragmentTransaction;

    public Fragmentation(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
        mFragmentTransaction = mFragmentManager.beginTransaction();
    }

    public static Fragmentation with(FragmentActivity activity) {
        return new Fragmentation(activity.getSupportFragmentManager());
    }

    public static Fragmentation with(@NonNull FragmentManager fragmentManager) {
        return new Fragmentation(fragmentManager);
    }

    public Fragmentation add(@IdRes int containerId, Fragment fragment) {
        return add(containerId, fragment, false);
    }

    public Fragmentation add(@IdRes int containerId, Fragment fragment, boolean isAddStack) {
        return add(containerId, fragment, isAddStack, fragment.getClass().getName());
    }

    public Fragmentation add(@IdRes int containerId, Fragment fragment, boolean isAddStack, @Nullable String tag) {
        Bundle args = getArgs(fragment);
        args.putInt(ARGS_CONTAINER_ID, containerId);
        Fragment fragmentByTag = mFragmentManager.findFragmentByTag(tag);
        if (fragmentByTag != null && fragmentByTag.isAdded()) {
            mFragmentTransaction.remove(fragmentByTag);
        }
        if (isAddStack) {
            mFragmentTransaction.addToBackStack(tag);
        }
        mFragmentTransaction.add(containerId, fragment, tag);
        return this;
    }

    public Fragmentation replace(@IdRes int containerId, Fragment targetFragment) {
        replace(containerId, targetFragment, false);
        return this;
    }

    public Fragmentation replace(@IdRes int containerId, Fragment targetFragment, boolean isAddStack) {
        replace(containerId, targetFragment, isAddStack, targetFragment.getClass().getName());
        return this;
    }

    public Fragmentation replace(@IdRes int containerId, Fragment targetFragment, boolean isAddStack, String tag) {
        if (isAddStack) {
            mFragmentTransaction.addToBackStack(tag);
        }
        mFragmentTransaction.replace(containerId, targetFragment, tag);
        return this;
    }

    public Fragmentation replace(@NonNull Fragment currentFragment, @NonNull final Fragment targetFragment) {
        replace(currentFragment, targetFragment, false);
        return this;
    }

    public Fragmentation replace(@NonNull Fragment currentFragment, @NonNull final Fragment targetFragment,
            boolean isAddStack) {
        replace(currentFragment, targetFragment, targetFragment.getClass().getName(), isAddStack);
        return this;
    }

    public Fragmentation replace(Fragment currentFragment, Fragment targetFragment, String tag, boolean isAddStack) {
        Bundle currentArgs = currentFragment.getArguments();
        if (currentArgs == null) {
            return this;
        }

        int containerId = currentArgs.getInt(ARGS_CONTAINER_ID);

        Bundle targetArgs = getArgs(targetFragment);
        targetArgs.putInt(ARGS_CONTAINER_ID, containerId);

        if (isAddStack) {
            mFragmentTransaction.addToBackStack(tag);
        }
        mFragmentTransaction.replace(containerId, targetFragment, tag);
        return this;
    }

    public Fragmentation show(@NonNull final Fragment... fragments) {
        for (Fragment fragment : fragments) {
            mFragmentTransaction.show(fragment);
        }
        return this;
    }

    public Fragmentation show(@NonNull final Class<? extends Fragment>... fragmentClass) {
        for (Class<? extends Fragment> aClass : fragmentClass) {
            Fragment fragment = findFragment(mFragmentManager, aClass);
            if (fragment != null) {
                mFragmentTransaction.show(fragment);
            }
        }
        return this;
    }

    public Fragmentation show(@NonNull final String... tags) {
        for (String tag : tags) {
            Fragment fragment = findFragment(mFragmentManager, tag);
            if (fragment != null) {
                mFragmentTransaction.show(fragment);
            }
        }
        return this;
    }

    public Fragmentation hide(@NonNull final Fragment... fragments) {
        for (Fragment fragment : fragments) {
            mFragmentTransaction.hide(fragment);
        }
        return this;
    }

    public Fragmentation hide(@NonNull final Class<? extends Fragment>... fragmentClass) {
        for (Class<? extends Fragment> aClass : fragmentClass) {
            Fragment fragment = findFragment(mFragmentManager, aClass);
            if (fragment != null) {
                mFragmentTransaction.hide(fragment);
            }
        }
        return this;
    }

    public Fragmentation hide(@NonNull final String... tags) {
        for (String tag : tags) {
            Fragment fragment = findFragment(mFragmentManager, tag);
            if (fragment != null) {
                mFragmentTransaction.hide(fragment);
            }
        }
        return this;
    }

    public Fragmentation hideAll() {
        List<Fragment> fragments = getFragments(mFragmentManager);
        for (Fragment fragment : fragments) {
            mFragmentTransaction.hide(fragment);
        }
        return this;
    }

    public Fragmentation remove(@NonNull final Fragment... fragments) {
        for (Fragment fragment : fragments) {
            mFragmentTransaction.remove(fragment);
        }
        return this;
    }

    public Fragmentation remove(@NonNull final Class<? extends Fragment>... fragmentClass) {
        for (Class<? extends Fragment> aClass : fragmentClass) {
            Fragment fragment = findFragment(mFragmentManager, aClass);
            if (fragment != null) {
                mFragmentTransaction.remove(fragment);
            }
        }
        return this;
    }

    public Fragmentation remove(@NonNull final String... tags) {
        for (String tag : tags) {
            Fragment fragment = findFragment(mFragmentManager, tag);
            if (fragment != null) {
                mFragmentTransaction.remove(fragment);
            }
        }
        return this;
    }

    public Fragmentation removeAll() {
        List<Fragment> fragments = getFragments(mFragmentManager);
        for (Fragment fragment : fragments) {
            mFragmentTransaction.remove(fragment);
        }
        return this;
    }

    /**
     * add 和 replace 可能会覆盖，以最后调用的方法为准
     */
    public Fragmentation addToBackStack(@Nullable String name) {
        mFragmentTransaction.addToBackStack(name);
        return this;
    }

    /**
     * 写在 add or replace 前面
     */
    public Fragmentation setAnimations(final int targetFragmentEnter, final int targetFragmentPopExit) {
        mFragmentTransaction.setCustomAnimations(targetFragmentEnter, 0, 0, targetFragmentPopExit);
        return this;
    }

    /**
     * 写在 add or replace 前面
     */
    public Fragmentation setAnimations(final int targetFragmentEnter, final int currentFragmentPopExit,
            final int currentFragmentPopEnter, final int targetFragmentExit) {
        mFragmentTransaction.setCustomAnimations(targetFragmentEnter, currentFragmentPopExit
                , currentFragmentPopEnter, targetFragmentExit);
        return this;
    }

    public Fragmentation addSharedElement(View sharedElement, String sharedName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFragmentTransaction.addSharedElement(sharedElement, sharedName);
        }
        return this;
    }

    public void commit() {
        commit(true);
    }

    public void commit(boolean isAllowingStateLoss) {
        if (isAllowingStateLoss) {
            mFragmentTransaction.commitAllowingStateLoss();
        } else {
            mFragmentTransaction.commit();
        }
    }

    public void commitNow() {
        commitNow(true);
    }

    public void commitNow(boolean isAllowingStateLoss) {
        if (isAllowingStateLoss) {
            mFragmentTransaction.commitNowAllowingStateLoss();
        } else {
            mFragmentTransaction.commitNow();
        }
    }

    @NonNull
    public static List<Fragment> getFragments(@NonNull final FragmentManager fm) {
        List<Fragment> fragments = fm.getFragments();
        if (fragments == null || fragments.isEmpty()) {
            return Collections.emptyList();
        }
        return fragments;
    }

    public static void pop(@NonNull final FragmentManager fm) {
        pop(fm, true);
    }

    public static void pop(@NonNull final FragmentManager fm,
            final boolean isImmediate) {
        if (isImmediate) {
            fm.popBackStackImmediate();
        } else {
            fm.popBackStack();
        }
    }

    public static void popTo(@NonNull final FragmentManager fm, final Class<? extends Fragment> popClz) {
        popTo(fm, popClz.getName());
    }

    public static void popTo(@NonNull final FragmentManager fm, final String backStateName) {
        popTo(fm, backStateName, false, true);
    }

    public static void popTo(@NonNull final FragmentManager fm, final Class<? extends Fragment> popClz,
            final boolean isIncludeSelf) {
        popTo(fm, popClz.getName(), isIncludeSelf);
    }

    public static void popTo(@NonNull final FragmentManager fm, final String stateName, final boolean isIncludeSelf) {
        popTo(fm, stateName, isIncludeSelf, true);
    }

    public static void popTo(@NonNull final FragmentManager fm, final String backStateName,
            final boolean isIncludeSelf, final boolean isImmediate) {
        if (isImmediate) {
            fm.popBackStackImmediate(backStateName,
                    isIncludeSelf ? FragmentManager.POP_BACK_STACK_INCLUSIVE : 0);
        } else {
            fm.popBackStack(backStateName,
                    isIncludeSelf ? FragmentManager.POP_BACK_STACK_INCLUSIVE : 0);
        }
    }

    public static void popAll(@NonNull final FragmentManager fm) {
        popAll(fm, true);
    }

    public static void popAll(@NonNull final FragmentManager fm, final boolean isImmediate) {
        if (fm.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry entry = fm.getBackStackEntryAt(0);
            if (isImmediate) {
                fm.popBackStackImmediate(entry.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                fm.popBackStack(entry.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }

    private static Bundle getArgs(final Fragment fragment) {
        Bundle bundle = fragment.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        return bundle;
    }

    @Nullable
    public static Fragment findFragment(FragmentManager fragmentManager,
            @NonNull Class<? extends Fragment> fragmentClass) {
        return findFragment(fragmentManager, fragmentClass.getName());
    }

    @Nullable
    public static Fragment findFragment(FragmentManager fragmentManager, @NonNull final String tag) {
        return fragmentManager.findFragmentByTag(tag);
    }

    public static void onBackPressedSupport(FragmentActivity activity) {
        if (activity.getSupportFragmentManager().getBackStackEntryCount() > 1) {
            activity.getSupportFragmentManager().popBackStack();
        } else {
            ActivityCompat.finishAfterTransition(activity);
        }
    }
}
