package com.test.gunnzo.gamefit;

/**
 * Used to identify how a user wants to create a new game
 * @author Gunnar
 * @version %G% 24.2.2018.
 */
public interface OnNewGameRequest {
    /**
     * Controls which new game dialog should be activated depending on the option
     * @param option an integer id representing the option chosen to create/join a new game
     */
    public void onOptionSelected(int option);
}
