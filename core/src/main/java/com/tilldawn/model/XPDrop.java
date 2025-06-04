package com.tilldawn.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class XPDrop {
    private static final float WIDTH  = 16f;
    private static final float HEIGHT = 16f;

    private final Texture    texture;
    private final float      x, y;      // bottom‐left corner in world‐coords
    private final int        xpValue;
    private final Rectangle  bounds;

    public XPDrop(Texture tex, float centerX, float centerY, int xpValue) {
        this.texture = tex;
        this.x       = centerX - WIDTH/2f;
        this.y       = centerY - HEIGHT/2f;
        this.xpValue = xpValue;
        this.bounds  = new Rectangle(this.x, this.y, WIDTH, HEIGHT);
    }

    public boolean collidesWith(Rectangle playerBounds) {
        return bounds.overlaps(playerBounds);
    }

    public int getXpValue() {
        return xpValue;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    /** Expose the underlying texture so we can draw it at screen coords. */
    public Texture getTexture() {
        return texture;
    }

    /** Expose world‐space X and Y so we can convert to screen coords. */
    public float getWorldX() { return x; }
    public float getWorldY() { return y; }

    /** These are the drop’s on‐screen dimensions (in world units). */
    public static float getWidth()  { return WIDTH; }
    public static float getHeight() { return HEIGHT; }

    public void dispose() {
        // We assume `texture` is shared (GameAssetsManager.getDrop()), so do not dispose here.
    }
}
