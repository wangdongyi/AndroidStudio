package wdy.business.util;

import android.support.v4.content.ContextCompat;
import android.view.View;

import com.balysv.materialripple.MaterialRippleLayout;

import wdy.business.R;

/**
 * 作者：王东一
 * 创建时间：2017/10/18.
 */

public class MaterialUtil {
    public static void Material(View view) {
        MaterialRippleLayout.on(view)
                .rippleOverlay(true)
                .rippleColor(ContextCompat.getColor(view.getContext(), R.color.half_transparent))
                .rippleAlpha(0.2f)
                .rippleHover(true)
                .create();
    }
    public static void MaterialRound(View view,int round) {
        MaterialRippleLayout.on(view)
                .rippleOverlay(true)
                .rippleColor(ContextCompat.getColor(view.getContext(), R.color.half_transparent))
                .rippleAlpha(0.2f)
                .rippleHover(true)
                .rippleRoundedCorners(CodeUtil.dip2px(view.getContext(),round))
                .create();
    }
    public static void MaterialBigRound(View view) {
        MaterialRippleLayout.on(view)
                .rippleOverlay(true)
                .rippleColor(ContextCompat.getColor(view.getContext(), R.color.half_transparent))
                .rippleAlpha(0.2f)
                .rippleHover(true)
                .rippleRoundedCorners(CodeUtil.dip2px(view.getContext(),45))
                .create();
    }
    public static void MaterialRound(View view) {
        MaterialRippleLayout.on(view)
                .rippleOverlay(true)
                .rippleColor(ContextCompat.getColor(view.getContext(), R.color.half_transparent))
                .rippleAlpha(0.2f)
                .rippleHover(true)
                .rippleRoundedCorners(CodeUtil.dip2px(view.getContext(),5))
                .create();
    }
    public static void MaterialOval(View view) {
        MaterialRippleLayout.on(view)
                .rippleOverlay(true)
                .rippleColor(ContextCompat.getColor(view.getContext(), R.color.half_transparent))
                .rippleAlpha(0.2f)
                .rippleHover(true)
                .rippleRoundedCorners(CodeUtil.dip2px(view.getContext(),100))
                .create();
    }
}
