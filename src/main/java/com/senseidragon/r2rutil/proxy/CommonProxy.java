/*
 * R2RUtils - CommonProxy
 *
 *  Copyright (c) 2015. Jesse Patterson ("Senseidragon")
 *
 *  @author Senseidragon
 *  @license Lesser GNU Public License v3 (http://www.gnu.org/licensese/lgpl.html)
 */

package com.senseidragon.r2rutil.proxy;

// I always hated "CommonProxy" basically being the ClientProxy
// So I made CommonProxy be abstract and then made ClientProxy and ServerProxy
// both extend this.  Purely semantic but I prefer it.
public abstract class CommonProxy implements IProxy {
}
