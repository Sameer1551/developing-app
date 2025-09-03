package com.example.myapplication.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.semantics.Role
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.unit.sp // Added for line height

@Composable
fun ReportIssueScreen() {
    // Form state variables
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var waterSourceName by remember { mutableStateOf("") }
    var sourceType by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var coordinates by remember { mutableStateOf("") }
    
    // Water observations
    var waterAppearance by remember { mutableStateOf("") }
    var waterSmell by remember { mutableStateOf("") }
    var waterTaste by remember { mutableStateOf("") }
    var visibleParticles by remember { mutableStateOf("") }
    var waterFlow by remember { mutableStateOf("") }
    
    // Health concerns (optional)
    var generalHealthIssues by remember { mutableStateOf("") }
    var skinProblems by remember { mutableStateOf("") }
    var stomachProblems by remember { mutableStateOf("") }
    
    // Additional notes
    var additionalNotes by remember { mutableStateOf("") }
    
    // UI state
    var isSubmitting by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    
    val scrollState = rememberScrollState()
    val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    
    // Auto-populate date
    LaunchedEffect(Unit) {
        // Date is auto-populated
    }
    
    if (showSuccess) {
        SuccessScreen {
            showSuccess = false
            // Reset form
            fullName = ""
            email = ""
            phone = ""
            waterSourceName = ""
            sourceType = ""
            location = ""
            coordinates = ""
            waterAppearance = ""
            waterSmell = ""
            waterTaste = ""
            visibleParticles = ""
            waterFlow = ""
            generalHealthIssues = ""
            skinProblems = ""
            stomachProblems = ""
            additionalNotes = ""
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 20.dp) // Adjusted padding
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp) // Added consistent spacing
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Science,
                    contentDescription = "Water Quality Report",
                    modifier = Modifier.size(36.dp), // Slightly larger icon
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Water Quality Report",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold // Ensure bold is applied if not default in theme
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Spacer(modifier = Modifier.height(8.dp)) // Removed, handled by Column's spacedBy
            
            Text(
                text = "Help protect your community by reporting water quality issues. Your detailed feedback is crucial.", // Slightly updated text
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 20.sp // Added for better readability
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant // Softer color
            )

            // Spacer(modifier = Modifier.height(24.dp)) // Removed, handled by Column's spacedBy
            
                        // 1. Personal Information Section
            FormSection(
                title = "Your Information",
                subtitle = "Required for follow-up and verification purposes." // Slightly rephrased
            ) {
                TextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name *") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Spacer(modifier = Modifier.height(16.dp)) // Removed, handled by FormSection's spacedBy

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email *") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Spacer(modifier = Modifier.height(16.dp)) // Removed, handled by FormSection's spacedBy

                TextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone *") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Spacer(modifier = Modifier.height(16.dp)) // Removed, handled by FormSection's spacedBy

                TextField(
                    value = currentDate,
                    onValueChange = { },
                    label = { Text("Date of Report") }, // Clarified label
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    colors = TextFieldDefaults.colors( // Styling for disabled field
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                )
            }
            
                        // Spacer(modifier = Modifier.height(24.dp)) // Removed, handled by Column's spacedBy

            // 2. Water Source Information Section
            FormSection(
                title = "Water Source Details", // Renamed for clarity
                subtitle = "Identify the specific water source being reported."
            ) {
                TextField(
                    value = waterSourceName,
                    onValueChange = { waterSourceName = it },
                    label = { Text("Water Source Name *") },
                    placeholder = { Text("e.g., Municipal Tap, Village Well, River Ganga") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Spacer(modifier = Modifier.height(16.dp)) // Removed, handled by FormSection's spacedBy

                SourceTypeDropdown(
                    selectedType = sourceType,
                    onTypeSelected = { sourceType = it }
                )

                // Spacer(modifier = Modifier.height(16.dp)) // Removed, handled by FormSection's spacedBy

                TextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location/Address *") },
                    placeholder = {Text("Street, Village/Town, District")}, // Added placeholder
                    modifier = Modifier.fillMaxWidth()
                )

                // Spacer(modifier = Modifier.height(16.dp)) // Removed, handled by FormSection's spacedBy

                TextField(
                    value = coordinates,
                    onValueChange = { coordinates = it },
                    label = { Text("GPS Coordinates (Optional)") }, // Clarified label
                    placeholder = { Text("e.g., 28.7041° N, 77.1025° E") }, // Added placeholder example
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // Spacer(modifier = Modifier.height(24.dp)) // Removed, handled by Column's spacedBy

            // 3. Water Observations Section
            FormSection(
                title = "Water Quality Observations", // Renamed for clarity
                subtitle = "Describe what you see, smell, or taste. No technical expertise needed!"
            ) {
                WaterAppearanceDropdown(
                    selectedAppearance = waterAppearance,
                    onAppearanceSelected = { waterAppearance = it }
                )

                // Spacer(modifier = Modifier.height(16.dp)) // Removed, handled by FormSection's spacedBy

                WaterSmellDropdown(
                    selectedSmell = waterSmell,
                    onSmellSelected = { waterSmell = it }
                )

                // Spacer(modifier = Modifier.height(16.dp)) // Removed, handled by FormSection's spacedBy

                WaterTasteDropdown(
                    selectedTaste = waterTaste,
                    onTasteSelected = { waterTaste = it }
                )

                // Spacer(modifier = Modifier.height(16.dp)) // Removed, handled by FormSection's spacedBy

                VisibleParticlesDropdown(
                    selectedParticles = visibleParticles,
                    onParticlesSelected = { visibleParticles = it }
                )

                // Spacer(modifier = Modifier.height(16.dp)) // Removed, handled by FormSection's spacedBy

                WaterFlowDropdown(
                    selectedFlow = waterFlow,
                    onFlowSelected = { waterFlow = it }
                )
            }
            
                        // Spacer(modifier = Modifier.height(24.dp)) // Removed, handled by Column's spacedBy

            // 4. Health Concerns Section (Optional)
            FormSection(
                title = "Health Concerns (If Any)", // Renamed
                subtitle = "Note any health issues potentially linked to this water source."
            ) {
                    TextField(
                    value = generalHealthIssues,
                    onValueChange = { generalHealthIssues = it },
                    label = { Text("General Health Symptoms") }, // Clarified label
                    placeholder = { Text("e.g., headaches, dizziness, fatigue, nausea") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Spacer(modifier = Modifier.height(16.dp)) // Removed, handled by FormSection's spacedBy

                TextField(
                    value = skinProblems,
                    onValueChange = { skinProblems = it },
                    label = { Text("Skin Related Symptoms") }, // Clarified label
                    placeholder = { Text("e.g., rash, itching, dryness, irritation") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Spacer(modifier = Modifier.height(16.dp)) // Removed, handled by FormSection's spacedBy

                    TextField(
                    value = stomachProblems,
                    onValueChange = { stomachProblems = it },
                    label = { Text("Stomach Related Symptoms") }, // Clarified label
                    placeholder = { Text("e.g., diarrhea, vomiting, cramps, bloating") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Spacer(modifier = Modifier.height(24.dp)) // Removed, handled by Column's spacedBy

            // 5. Photos Section (Optional)
            FormSection(
                title = "Upload Photos (Optional)",
                subtitle = "Images can significantly help in assessing the issue."
            ) {
                PhotoUploadSection()
            }

            // Spacer(modifier = Modifier.height(24.dp)) // Removed, handled by Column's spacedBy

            // 6. Additional Notes Section
            FormSection(
                title = "Additional Notes/Observations", // Renamed
                subtitle = "Share any other details or concerns you might have."
            ) {
                OutlinedTextField( // Changed to OutlinedTextField for consistency
                    value = additionalNotes,
                    onValueChange = { additionalNotes = it },
                    label = { Text("Additional Notes") },
                    placeholder = { Text("Include any other relevant information here...") },
                        modifier = Modifier.fillMaxWidth(),
                    minLines = 4 // Increased minLines
                )
            }
            
            // Spacer(modifier = Modifier.height(32.dp)) // Removed, handled by Column's spacedBy

            // Submit Button
            val coroutineScope = rememberCoroutineScope()
            Button(
                onClick = {
                    isSubmitting = true
                    // Simulate submission
                    coroutineScope.launch {
                        delay(2000)
                        isSubmitting = false
                        showSuccess = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), // Added vertical padding
                enabled = !isSubmitting && fullName.isNotBlank() && email.isNotBlank() && // Used isNotBlank
                         phone.isNotBlank() && waterSourceName.isNotBlank() &&
                         sourceType.isNotBlank() && location.isNotBlank() &&
                         waterAppearance.isNotBlank() && waterSmell.isNotBlank() &&
                         waterTaste.isNotBlank() && visibleParticles.isNotBlank() &&
                         waterFlow.isNotBlank(),
                contentPadding = PaddingValues(vertical = 12.dp) // Increased button padding
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp), // Slightly larger indicator
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp // Thinner stroke
                    )
                    Spacer(modifier = Modifier.width(12.dp)) // Increased spacing
                    Text("Submitting Report...")
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Send, // Changed Icon
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submit Water Quality Report")
                }
            }

            // Add bottom padding to ensure content doesn't go behind navigation bar
            Spacer(modifier = Modifier.height(120.dp)) // Ensures submit button is visible above navigation bar
        }
    }
}



@Composable
private fun FormSection(
    title: String,
    subtitle: String,
    content: @Composable ColumnScope.() -> Unit
) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant // Changed for better contrast
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp), // Reduced elevation for a flatter, modern look
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)) // Subtle border
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp), // Adjusted padding
            verticalArrangement = Arrangement.spacedBy(16.dp) // Consistent spacing for content
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge, // Made title larger
                // fontWeight = FontWeight.Bold, // TitleLarge might already be bold in theme
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Spacer(modifier = Modifier.height(4.dp)) // Removed, handled by Column's spacedBy

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium, // Used bodyMedium for subtitle
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                lineHeight = 18.sp // Added line height
            )

            // Spacer(modifier = Modifier.height(16.dp)) // Removed, handled by Column's spacedBy
            // Adding a small spacer before the content for visual separation from subtitle
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SourceTypeDropdown(
    selectedType: String,
    onTypeSelected: (String) -> Unit
) {
    val sourceTypes = listOf(
        "Municipal Tap Water", // Clarified
        "Community Well/Borewell", // Clarified
        "Private Well/Borewell", // Added option
        "River/Stream",
        "Lake/Pond",
        "Public Water Tanker", // Added option
        "Packaged Drinking Water", // Added option
        "Other"
    )

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField( // Used OutlinedTextField for consistency
            value = selectedType,
            onValueChange = { },
            readOnly = true,
            label = { Text("Source Type *") },
            placeholder = {Text("Select the type of water source")}, // Added placeholder
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium // Rounded corners
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            sourceTypes.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type) },
                    onClick = {
                        onTypeSelected(type)
                        expanded = false
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp) // Increased padding
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WaterAppearanceDropdown(
    selectedAppearance: String,
    onAppearanceSelected: (String) -> Unit
) {
    val appearances = listOf(
        "Clear and transparent",
        "Slightly cloudy or hazy", // "or" instead of "/"
        "Very cloudy or murky", // "or" instead of "/"
        "Has unusual color (e.g., yellow, brown, green)", // Added e.g.
        "Oily or greasy film on surface", // "film on" instead of "surface"
        "Foamy or bubbly (persistent)" // Added persistent
    )

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedAppearance,
            onValueChange = { },
            readOnly = true,
            label = { Text("Water Appearance *") },
            placeholder = {Text("Describe how the water looks")},
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            appearances.forEach { appearance ->
                DropdownMenuItem(
                    text = { Text(appearance) },
                    onClick = {
                        onAppearanceSelected(appearance)
                        expanded = false
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WaterSmellDropdown(
    selectedSmell: String,
    onSmellSelected: (String) -> Unit
) {
    val smells = listOf(
        "No unusual smell",
        "Strong chlorine smell (like a swimming pool)", // Added context
        "Rotten egg smell (sulfur)", // Added context
        "Earthy or musty smell",
        "Chemical or industrial smell (e.g., solvents, fuel)", // Added e.g.
        "Sewage or waste smell",
        "Other unusual smell (please specify in notes)" // Added instruction
    )
    
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedSmell,
            onValueChange = { },
            readOnly = true,
            label = { Text("Water Smell *") },
            placeholder = {Text("Describe any odors from the water")},
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            smells.forEach { smell ->
                DropdownMenuItem(
                    text = { Text(smell) },
                    onClick = {
                        onSmellSelected(smell)
                        expanded = false
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WaterTasteDropdown(
    selectedTaste: String,
    onTasteSelected: (String) -> Unit
) {
    val tastes = listOf(
        "Normal taste (no unusual taste)", // Clarified
        "Metallic or iron taste",
        "Salty taste",
        "Bitter taste",
        "Unusually sweet taste", // Added "taste"
        "Chemical taste (e.g., plastic, medicinal)", // Added e.g.
        "Earthy or musty taste", // Added option
        "Other unusual taste (please specify in notes)" // Added instruction
    )
    
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedTaste,
            onValueChange = { },
            readOnly = true,
            label = { Text("Water Taste *") },
            placeholder = {Text("Describe any unusual tastes")},
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            tastes.forEach { taste ->
                DropdownMenuItem(
                    text = { Text(taste) },
                    onClick = {
                        onTasteSelected(taste)
                        expanded = false
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VisibleParticlesDropdown(
    selectedParticles: String,
    onParticlesSelected: (String) -> Unit
) {
    val particles = listOf(
        "No visible particles or sediment", // Clarified
        "A few small, fine particles",
        "Many visible particles or specks", // "or specks"
        "Sediment or sand settled at the bottom",
        "Floating debris or larger particles",
        "Rust or metal-like particles", // Added option
        "Worms or other small living organisms" // Clarified
    )
    
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedParticles,
            onValueChange = { },
            readOnly = true,
            label = { Text("Visible Particles/Sediment *") }, // Clarified label
            placeholder = {Text("Describe any visible matter in the water")},
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            particles.forEach { particle ->
                DropdownMenuItem(
                    text = { Text(particle) },
                    onClick = {
                        onParticlesSelected(particle)
                        expanded = false
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WaterFlowDropdown(
    selectedFlow: String,
    onFlowSelected: (String) -> Unit
) {
    val flows = listOf(
        "Normal flow and pressure", // Clarified
        "Slow or reduced flow",
        "No flow (dry tap/well)",
        "Irregular or intermittent flow (sputtering)", // Added context
        "Very high pressure (splashing)", // Added context
        "Very low pressure (trickle)" // Added context
    )
    
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedFlow,
            onValueChange = { },
            readOnly = true,
            label = { Text("Water Flow/Pressure *") }, // Clarified label
            placeholder = {Text("Describe the water flow from the source")},
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            flows.forEach { flow ->
                DropdownMenuItem(
                    text = { Text(flow) },
                    onClick = {
                        onFlowSelected(flow)
                        expanded = false
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun PhotoUploadSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f) // Lighter, distinct background
        ),
        shape = MaterialTheme.shapes.large, // More rounded corners
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondaryContainer) // Subtle border matching container
            ) {
                Column(
                    modifier = Modifier
                .padding(20.dp) // Increased padding
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp) // Spacing for inner elements
        ) {
            Icon(
                imageVector = Icons.Outlined.FileUpload, // Changed icon for more direct meaning
                contentDescription = "Upload Photos",
                modifier = Modifier.size(56.dp), // Larger icon
                tint = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f) // Adjusted tint for contrast
            )

            // Spacer(modifier = Modifier.height(8.dp)) // Handled by Column's spacedBy

            Text(
                text = "Tap to Upload Photos", // Clearer call to action
                style = MaterialTheme.typography.titleSmall, // More prominent text
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            // Spacer(modifier = Modifier.height(4.dp)) // Handled by Column's spacedBy

            Text(
                text = "Supports JPG, PNG. Max 10MB per photo.", // Simplified text
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
            )

            // Spacer(modifier = Modifier.height(12.dp)) // Handled by Column's spacedBy

                    Text(
                text = "Photo Suggestions:", // Simplified
                style = MaterialTheme.typography.labelLarge, // More prominent label
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            // Spacer(modifier = Modifier.height(8.dp)) // Handled by Column's spacedBy

            val suggestions = listOf(
                "Color of the water", // Rephrased
                "Any visible particles or cloudiness", // Rephrased
                "The water source itself (tap, well, etc.)", // Rephrased
                "Area around the water source if relevant" // Rephrased
            )

            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) { // Align suggestions to start
                suggestions.forEach { suggestion ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp), // Increased vertical padding
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                            imageVector = Icons.Outlined.PhotoCamera,
                                contentDescription = null,
                            modifier = Modifier.size(18.dp), // Slightly larger suggestion icon
                            tint = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                            )
                        Spacer(modifier = Modifier.width(8.dp))
                            Text(
                            text = suggestion,
                            style = MaterialTheme.typography.bodyMedium, // Slightly larger suggestion text
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SuccessScreen(onDismiss: () -> Unit) {
    // Auto-dismiss after a delay
    LaunchedEffect(Unit) {
        delay(3500) // Slightly longer delay
        onDismiss()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Added padding to the box itself
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth() // Allow card to take reasonable width
                .padding(horizontal = 24.dp), // Horizontal padding for the card
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer // Changed color for variety
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), // More pronounced elevation
            shape = MaterialTheme.shapes.large // Rounded corners
        ) {
            Column(
                modifier = Modifier.padding(all = 32.dp), // Consistent padding
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp) // Spacing for inner elements
            ) {
                Icon(
                    imageVector = Icons.Outlined.TaskAlt, // Changed icon
                    contentDescription = "Success",
                    modifier = Modifier.size(72.dp), // Larger icon
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )

                // Spacer(modifier = Modifier.height(16.dp)) // Handled by Column's spacedBy

                Text(
                    text = "Report Submitted Successfully!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    textAlign = TextAlign.Center
                )

                // Spacer(modifier = Modifier.height(8.dp)) // Handled by Column's spacedBy

                Text(
                    text = "Thank you for your contribution! We will review your report and provide updates via email.", // Updated message
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.85f), // Slightly more opaque
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp // Added line height for readability
                )
            }
        }
    }
}
