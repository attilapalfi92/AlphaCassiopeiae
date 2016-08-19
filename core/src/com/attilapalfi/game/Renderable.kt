package com.attilapalfi.game

import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.Disposable

/**
 * Created by palfi on 2016-04-02.
 */
interface Renderable : Disposable {
    fun render(projAndViewMatrix: Matrix4);
}