# Bottom Navigation Content Overlap Fix

## Problem
When users scrolled down in the Settings, Help & Support, and About screens (accessed from the Profile screen), the content was getting hidden behind the bottom navigation bar, making it difficult to read the last items.

## Solution
Added proper bottom padding to all screens that are displayed within the ProfileScreen to ensure content doesn't get hidden behind the bottom navigation.

## Changes Made

### 1. ProfileScreen.kt
- Wrapped SettingsScreen, HelpSupportScreen, and AboutScreen in Box containers with bottom padding
- Added `padding(bottom = 96.dp)` to account for the bottom navigation height

### 2. SettingsScreen.kt
- Added a Spacer with `height(120.dp)` at the end of the main Column
- This ensures the last settings items are fully visible when scrolling

### 3. HelpSupportScreen.kt
- Added a Spacer with `height(120.dp)` at the end of the main Column
- This ensures FAQ items and contact information are fully visible when scrolling

### 4. AboutScreen.kt
- Added a Spacer with `height(120.dp)` at the end of the main Column
- This ensures all about information is fully visible when scrolling

### 5. ReportIssueScreen.kt
- Added a Spacer with `height(120.dp)` before the submit button section
- This ensures the form content is fully visible when scrolling

## Technical Details

### Bottom Navigation Height
The bottom navigation has a height of 80dp plus padding, so we used:
- **96dp** for the ProfileScreen wrapper (accounts for navigation + some extra space)
- **120dp** for internal screen spacers (provides comfortable scrolling space)

### Screens Already Fixed
The following screens already had proper bottom padding:
- **AlertScreen**: Uses `contentPadding = PaddingValues(bottom = 100.dp)` in LazyColumn
- **AwarenessScreen**: Has a Spacer with `height(120.dp)` at the end
- **HomeScreen**: Uses `contentPadding = PaddingValues(bottom = 100.dp)` in LazyColumn

## Result
✅ All screens now have proper bottom padding
✅ Content is no longer hidden behind the bottom navigation
✅ Users can scroll to the bottom of any screen and see all content
✅ Consistent user experience across all screens

## Testing
- Build successful with no compilation errors
- All screens should now display content properly without overlap
- Dark mode functionality remains intact
