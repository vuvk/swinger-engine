/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vuvk.swinger.objects.creatures.enemy;

import java.io.Serializable;

/**
 *
 * @author tai-prg3
 */
public enum EnemyState implements Serializable {
    IDLE,
    WALK,
    ATTACK,
    PAIN,
    DIE,
    //DEAD
}
