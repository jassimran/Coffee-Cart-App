package gatech.cs6300.project2;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;

public class DateFormattingTextWatcher implements TextWatcher 
{

    private boolean mFormatting;
    private boolean mDeletingSlash;
    private int mSlashStart;    
    private boolean mDeletingBackward;
    
    
    public DateFormattingTextWatcher()
    {
        
    }
    @Override
    public synchronized void afterTextChanged(Editable text) 
    {
        if(!mFormatting) 
        {
            mFormatting = true;
            // If deleting the hyphen, also delete the char before or after that
            if (mDeletingSlash && mSlashStart > 0) 
            {
                if (mDeletingBackward) 
                {
                    if (mSlashStart - 1 < text.length()) 
                    {
                        text.delete(mSlashStart - 1, mSlashStart);
                    }
                } 
                else if (mSlashStart < text.length()) 
                {
                    text.delete(mSlashStart, mSlashStart + 1);
                }
            }
            
            formatDate(text);
            mFormatting = false;
        }
        
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) 
    {
        // Check if the user is deleting a hyphen
        if (!mFormatting) 
        {
            // Make sure user is deleting one char, without a selection
            final int selStart = Selection.getSelectionStart(s);
            final int selEnd = Selection.getSelectionEnd(s);
            if (s.length() > 1 // Can delete another character 
                    && count == 1 // Deleting only one character 
                    && after == 0 // Deleting
                    && s.charAt(start) == '-' // a hyphen
                    && selStart == selEnd) 
            { // no selection
                mDeletingSlash = true;
                mSlashStart = start;
                // Check if the user is deleting forward or backward
                if (selStart == start + 1) 
                {
                    mDeletingBackward = true;
                } 
                else 
                {
                    mDeletingBackward = false;
                }
            } 
            else 
            {
                mDeletingSlash = false;
            }
        }        
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) 
    {
        // TODO Auto-generated method stub
        
    }
    
     /**
     * Formats a date in the format MM/DD/YYYY
     */
    public static void formatDate(Editable text) 
    {
        int length = text.length();
        if (length > "MM/DD/YYYY".length()) 
        {
            // The string is too long to be formatted
            return;
        }
        CharSequence saved = text.subSequence(0, length);

        // Strip the dashes first, as we're going to add them back
        int p = 0;
        while (p < text.length()) 
        {
            if (text.charAt(p) == '/' || text.charAt(p) == '-' || text.charAt(p) == '.') 
            {
                text.delete(p, p + 1);
            } 
            else 
            {
                p++;
            }
        }
        
        length = text.length();

        // When scanning the number we record where dashes need to be added,
        // if they're non-0 at the end of the scan the dashes will be added in
        // the proper places.
        int dashPositions[] = new int[3];
        int numSlashes = 0;
        int numDigits = 0;
        
        for (int i = 0; i < length; i++) 
        {
            char c = text.charAt(i);
            switch (c) {
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '0':
                    if (numDigits == 2 || numDigits == 4) 
                    {
                        // Found a digit that should be after a dash that isn't
                        dashPositions[numSlashes++] = i;
                    }
                    numDigits++;
                    break;
                default:
                    // Unknown character, bail on formatting
                    text.replace(0, length, saved);
                    return;
            }
        }

        // Actually put the slashes in place
        for (int i = 0; i < numSlashes; i++) 
        {
            int pos = dashPositions[i];
            text.replace(pos + i, pos + i, "/");
        }

        // Remove trailing dashes
        int len = text.length();
        while (len > 0) 
        {
            if (text.charAt(len - 1) == '/') 
            {
                text.delete(len - 1, len);
                len--;
            } 
            else 
            {
                break;
            }
        }
    }
}
