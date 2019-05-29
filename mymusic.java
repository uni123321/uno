package myuno;

import java.io.*;

import javax.sound.midi.*;
import javax.swing.JOptionPane;

public class mymusic implements Runnable {
	private Sequencer midi;
	
	private boolean started;
	
	public mymusic ()
	{
		started = false;
	}
	
	// ‘›Õ£∫Ø ˝
	public static void mysleep(int tim)
	{
		try {
			Thread.sleep(tim);
		} catch (InterruptedException e) {
			
		}
	}
	
	public void run()
	{
		try {
            midi = MidiSystem.getSequencer();
            midi.open();
			Sequence se = MidiSystem.getSequence(new File("bgm.mid"));
			midi.setSequence(se);
			midi.setLoopStartPoint(1900);
            midi.setLoopEndPoint(-1);
            midi.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            mysleep(3500);
            midi.start();
            started = true;
		} catch (Exception e) {
			mysleep(2500);
			JOptionPane.showMessageDialog(null, "±≥æ∞“Ù¿÷≤•∑≈ ß∞‹£°", "¥ÌŒÛ", JOptionPane.ERROR_MESSAGE);
			started = false;
		}
	}
	
	public void changestate()
	{
		if (started)
		{				
			if (midi.isRunning())
				midi.stop();
			else
				midi.start();
		}
	}
	
}

