package com.aarondevelops.alma_mater.AudioUtils;

import com.aarondevelops.alma_mater.Framework.Note;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Aaron K on 4/12/2017.
 */

public abstract class NoteFactory
{

    private static ArrayList<NoteInfo> allNotes = new ArrayList<>();

    static
    {
        initializeAllNotes();
    }

    public static Note createNote(double frequency)
    {
        NoteInfo determinedNote = determineNote(frequency);
        return determinedNote;
    }

    private static NoteInfo determineNote(double noteFrequency)
    {
        NoteInfo selectedNote = new NoteInfo(0.0, "-  ");
        selectedNote.normalize(noteFrequency);
        if(noteFrequency == -1.0)
        {
            return selectedNote;
        }

        for (int i = 0; i < allNotes.size() - 1; i++)
        {
            NoteInfo currentNote = allNotes.get(i);
            NoteInfo nextNote = allNotes.get(i + 1);

            // if the current note is less and next is greater
            if(currentNote.getFrequency() <= noteFrequency
                    && nextNote.getFrequency() >= noteFrequency)
            {
                selectedNote = getClosestNote(noteFrequency, currentNote, nextNote);
                selectedNote.normalize(noteFrequency);
            }
        }

        return selectedNote;
    }

    private static NoteInfo getClosestNote(double noteFrequency, NoteInfo lowerNote, NoteInfo higherNote)
    {
        double distanceFromLower = noteFrequency - lowerNote.getFrequency();
        double distanceFromHigher = higherNote.getFrequency() - noteFrequency;

        if(distanceFromLower < distanceFromHigher)
        {
            return lowerNote;
        }
        else
        {
            return higherNote;
        }
    }

    private static class NoteInfo implements Note
    {
        double frequency;
        double normalizedValue;
        char note;
        char pitch;
        char octave;

        public NoteInfo(double frequency, String noteRepresentation)
        {
            if(noteRepresentation.length() != 3)
            {
                throw new IllegalArgumentException
                        ("String must be in the format '<Note><Pitch><Octave>'");
            }

            this.frequency = frequency;
            note = noteRepresentation.charAt(0);
            pitch = noteRepresentation.charAt(1);
            octave = noteRepresentation.charAt(2);
        }

        @Override
        public double getNormalizedValue()
        {
            return normalizedValue;
        }

        public char getNote()
        {
            return note;
        }

        public char getPitch()
        {
            return pitch;
        }

        public char getOctave()
        {
            return octave;
        }
        public double getFrequency()
        {
            return frequency;
        }
        public void normalize(double actualFrequency)
        {
            if(actualFrequency == - 1.0)
            {
                normalizedValue = .5;
                return;
            }

            NoteInfo previousNote = new NoteInfo(0.0, "-  ");
            NoteInfo nextNote = new NoteInfo(8500, "-  ");

            Iterator noteIterator = allNotes.iterator();
            while(noteIterator.hasNext())
            {
                NoteInfo currentNote = (NoteInfo) noteIterator.next();
                if(currentNote.equals(this))
                {
                    if(noteIterator.hasNext())
                    {
                        nextNote = (NoteInfo) noteIterator.next();
                    }

                    break; // note found, stop
                }

                previousNote = currentNote;
            }

            double min = (this.frequency + previousNote.frequency) / 2;
            double max = (this.frequency + nextNote.frequency) / 2;
            this.normalizedValue = (actualFrequency - min) / (max - min);
        }

        public boolean equals(NoteInfo n)
        {
            if(this.frequency == n.frequency)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    private static void initializeAllNotes()
    {
        allNotes.add(new NoteInfo(16.35, "C 0"));
        allNotes.add(new NoteInfo(17.32, "C#0"));
        allNotes.add(new NoteInfo(18.35, "D 0"));
        allNotes.add(new NoteInfo(19.45, "D#0"));
        allNotes.add(new NoteInfo(20.60, "E 0"));
        allNotes.add(new NoteInfo(21.83, "F 0"));
        allNotes.add(new NoteInfo(23.12, "F#0"));
        allNotes.add(new NoteInfo(24.50, "G 0"));
        allNotes.add(new NoteInfo(25.96, "G#0"));
        allNotes.add(new NoteInfo(27.50, "A 0"));
        allNotes.add(new NoteInfo(29.14, "A#0"));
        allNotes.add(new NoteInfo(30.87, "B 0"));
        allNotes.add(new NoteInfo(32.70, "C 1"));
        allNotes.add(new NoteInfo(34.65, "C#1"));
        allNotes.add(new NoteInfo(36.71, "D 1"));
        allNotes.add(new NoteInfo(38.89, "D#1"));
        allNotes.add(new NoteInfo(41.20, "E 1"));
        allNotes.add(new NoteInfo(43.65, "F 1"));
        allNotes.add(new NoteInfo(46.25, "F#1"));
        allNotes.add(new NoteInfo(49.00, "G 1"));
        allNotes.add(new NoteInfo(51.91, "G#1"));
        allNotes.add(new NoteInfo(55.00, "A 1"));
        allNotes.add(new NoteInfo(58.27, "A#1"));
        allNotes.add(new NoteInfo(61.74, "B 1"));
        allNotes.add(new NoteInfo(65.41, "C 2"));
        allNotes.add(new NoteInfo(69.30, "C#2"));
        allNotes.add(new NoteInfo(73.42, "D 2"));
        allNotes.add(new NoteInfo(77.78, "D#2"));
        allNotes.add(new NoteInfo(82.41, "E 2"));
        allNotes.add(new NoteInfo(87.31, "F 2"));
        allNotes.add(new NoteInfo(92.50, "F#2"));
        allNotes.add(new NoteInfo(98.00, "G 2"));
        allNotes.add(new NoteInfo(103.83, "G#2"));
        allNotes.add(new NoteInfo(110.00, "A 2"));
        allNotes.add(new NoteInfo(116.54, "A#2"));
        allNotes.add(new NoteInfo(123.47, "B 2"));
        allNotes.add(new NoteInfo(130.81, "C 3"));
        allNotes.add(new NoteInfo(138.59, "C#3"));
        allNotes.add(new NoteInfo(146.83, "D 3"));
        allNotes.add(new NoteInfo(155.56, "D#3"));
        allNotes.add(new NoteInfo(164.81, "E 3"));
        allNotes.add(new NoteInfo(174.61, "F 3"));
        allNotes.add(new NoteInfo(185.00, "F#3"));
        allNotes.add(new NoteInfo(196.00, "G 3"));
        allNotes.add(new NoteInfo(207.65, "G#3"));
        allNotes.add(new NoteInfo(220.00, "A 3"));
        allNotes.add(new NoteInfo(233.08, "A#3"));
        allNotes.add(new NoteInfo(246.94, "B 3"));
        allNotes.add(new NoteInfo(261.63, "C 4"));
        allNotes.add(new NoteInfo(277.18, "C#4"));
        allNotes.add(new NoteInfo(293.66, "D 4"));
        allNotes.add(new NoteInfo(311.13, "D#4"));
        allNotes.add(new NoteInfo(329.63, "E 4"));
        allNotes.add(new NoteInfo(349.23, "F 4"));
        allNotes.add(new NoteInfo(369.99, "F#4"));
        allNotes.add(new NoteInfo(392.00, "G 4"));
        allNotes.add(new NoteInfo(415.30, "G#4"));
        allNotes.add(new NoteInfo(440.00, "A 4"));
        allNotes.add(new NoteInfo(466.16, "A#4"));
        allNotes.add(new NoteInfo(493.88, "B 4"));
        allNotes.add(new NoteInfo(523.25, "C 5"));
        allNotes.add(new NoteInfo(554.37, "C#5"));
        allNotes.add(new NoteInfo(587.33, "D 5"));
        allNotes.add(new NoteInfo(622.25, "D#5"));
        allNotes.add(new NoteInfo(659.25, "E 5"));
        allNotes.add(new NoteInfo(698.46, "F 5"));
        allNotes.add(new NoteInfo(739.99, "F#5"));
        allNotes.add(new NoteInfo(783.99, "G 5"));
        allNotes.add(new NoteInfo(830.61, "G#5"));
        allNotes.add(new NoteInfo(880.00, "A 5"));
        allNotes.add(new NoteInfo(932.33, "A#5"));
        allNotes.add(new NoteInfo(987.77, "B 5"));
        allNotes.add(new NoteInfo(1046.50, "C 6"));
        allNotes.add(new NoteInfo(1108.73, "C#6"));
        allNotes.add(new NoteInfo(1174.66, "D 6"));
        allNotes.add(new NoteInfo(1244.51, "D#6"));
        allNotes.add(new NoteInfo(1318.51, "E 6"));
        allNotes.add(new NoteInfo(1396.91, "F 6"));
        allNotes.add(new NoteInfo(1479.98, "F#6"));
        allNotes.add(new NoteInfo(1567.98, "G 6"));
        allNotes.add(new NoteInfo(1661.22, "G#6"));
        allNotes.add(new NoteInfo(1760.00, "A 6"));
        allNotes.add(new NoteInfo(1864.66, "A#6"));
        allNotes.add(new NoteInfo(1975.53, "B 6"));
        allNotes.add(new NoteInfo(2093.00, "C 7"));
        allNotes.add(new NoteInfo(2217.46, "C#7"));
        allNotes.add(new NoteInfo(2349.32, "D 7"));
        allNotes.add(new NoteInfo(2489.02, "D#7"));
        allNotes.add(new NoteInfo(2637.02, "E 7"));
        allNotes.add(new NoteInfo(2793.83, "F 7"));
        allNotes.add(new NoteInfo(2959.96, "F#7"));
        allNotes.add(new NoteInfo(3135.96, "G 7"));
        allNotes.add(new NoteInfo(3322.44, "G#7"));
        allNotes.add(new NoteInfo(3520.00, "A 7"));
        allNotes.add(new NoteInfo(3729.31, "A#7"));
        allNotes.add(new NoteInfo(3951.07, "B 7"));
        allNotes.add(new NoteInfo(4186.01, "C 8"));
        allNotes.add(new NoteInfo(4434.92, "C#8"));
        allNotes.add(new NoteInfo(4698.63, "D 8"));
        allNotes.add(new NoteInfo(4978.03, "D#8"));
        allNotes.add(new NoteInfo(5274.04, "E 8"));
        allNotes.add(new NoteInfo(5587.65, "F 8"));
        allNotes.add(new NoteInfo(5919.91, "F#8"));
        allNotes.add(new NoteInfo(6271.93, "G 8"));
        allNotes.add(new NoteInfo(6644.88, "G#8"));
        allNotes.add(new NoteInfo(7040.00, "A 8"));
        allNotes.add(new NoteInfo(7458.62, "A#8"));
        allNotes.add(new NoteInfo(7902.13, "B 8"));
    }

}