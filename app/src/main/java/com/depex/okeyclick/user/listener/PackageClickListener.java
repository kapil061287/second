package com.depex.okeyclick.user.listener;

import com.depex.okeyclick.user.model.UserPackage;

/**
 * Created by we on 1/25/2018.
 */

public interface PackageClickListener {
    void onPackageClick(UserPackage userPackage);
    void onPackageLongClick(UserPackage userPackage);
    void onInfoClick(UserPackage userPackage);
}
