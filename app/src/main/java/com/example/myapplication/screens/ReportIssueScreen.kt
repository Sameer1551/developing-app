package com.example.myapplication.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.unit.sp // Added for line height
import android.content.Context
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.data.ReportStorage
import com.example.myapplication.models.WaterQualityReport

data class PhotoItem(
    val id: String,
    val name: String,
    val size: String,
    val timestamp: Date = Date()
)

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
    
    // Photo upload state
    val uploadedPhotos = remember { mutableStateListOf<PhotoItem>() }
    var showPhotoPicker by remember { mutableStateOf(false) }
    
    // UI state
    var isSubmitting by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    var showValidationError by remember { mutableStateOf(false) }
    var validationMessage by remember { mutableStateOf("") }
    
    val scrollState = rememberScrollState()
    val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    
    // Form validation function
    fun validateForm(): Boolean {
        val requiredFields = listOf(
            fullName to "Full Name",
            email to "Email",
            phone to "Phone Number",
            waterSourceName to "Water Source Name",
            sourceType to "Source Type",
            location to "Location",
            waterAppearance to "Water Appearance",
            waterSmell to "Water Smell",
            waterTaste to "Water Taste",
            visibleParticles to "Visible Particles",
            waterFlow to "Water Flow"
        )
        
        val emptyFields = requiredFields.filter { it.first.isBlank() }
        
        if (emptyFields.isNotEmpty()) {
            validationMessage = "Please fill in: ${emptyFields.joinToString(", ") { it.second }}"
            showValidationError = true
            return false
        }
        
        // Email validation
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            validationMessage = "Please enter a valid email address"
            showValidationError = true
            return false
        }
        
        // Phone validation (basic)
        if (phone.length < 10) {
            validationMessage = "Please enter a valid phone number (at least 10 digits)"
            showValidationError = true
            return false
        }
        
        showValidationError = false
        return true
    }
    
    // Get context and report storage
    val context = LocalContext.current
    val reportStorage = remember { ReportStorage(context) }
    
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
            uploadedPhotos.clear() // Clear uploaded photos
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp) // Reduced vertical padding
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp) // Reduced spacing between sections
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Science,
                    contentDescription = "Water Quality Report",
                    modifier = Modifier.size(28.dp), // Reduced icon size
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp)) // Reduced spacing
                Text(
                    text = "Water Quality Report",
                    style = MaterialTheme.typography.titleLarge.copy( // Reduced from headlineMedium
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Text(
                text = "Report water quality issues to help protect your community.", // Shortened description
                style = MaterialTheme.typography.bodySmall.copy( // Reduced from bodyMedium
                    lineHeight = 16.sp // Reduced line height
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                PhotoUploadSection(
                    uploadedPhotos = uploadedPhotos,
                    onAddPhoto = { showPhotoPicker = true },
                    onRemovePhoto = { photoId ->
                        uploadedPhotos.removeAll { it.id == photoId }
                    }
                )
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

            // Submit Button Section
            val coroutineScope = rememberCoroutineScope()
            
            // Form completion status
            val isFormComplete = remember(
                fullName, email, phone, waterSourceName, sourceType, location,
                waterAppearance, waterSmell, waterTaste, visibleParticles, waterFlow
            ) {
                fullName.isNotBlank() && email.isNotBlank() && phone.isNotBlank() &&
                waterSourceName.isNotBlank() && sourceType.isNotBlank() && location.isNotBlank() &&
                waterAppearance.isNotBlank() && waterSmell.isNotBlank() && waterTaste.isNotBlank() &&
                visibleParticles.isNotBlank() && waterFlow.isNotBlank()
            }
            
            // Submit Button
            Button(
                onClick = {
                    if (validateForm()) {
                        isSubmitting = true
                        // Create and save the water quality report
                        coroutineScope.launch {
                            try {
                                val report = WaterQualityReport(
                                    fullName = fullName,
                                    email = email,
                                    phone = phone,
                                    waterSourceName = waterSourceName,
                                    sourceType = sourceType,
                                    location = location,
                                    coordinates = coordinates,
                                    waterAppearance = waterAppearance,
                                    waterSmell = waterSmell,
                                    waterTaste = waterTaste,
                                    visibleParticles = visibleParticles,
                                    waterFlow = waterFlow,
                                    generalHealthIssues = generalHealthIssues,
                                    skinProblems = skinProblems,
                                    stomachProblems = stomachProblems,
                                    additionalNotes = additionalNotes,
                                    photoCount = uploadedPhotos.size
                                )
                                
                                val reportId = reportStorage.saveReport(report)
                                delay(1000) // Brief delay for UX
                                isSubmitting = false
                                showSuccess = true
                            } catch (e: Exception) {
                                // Handle error
                                validationMessage = "Failed to save report: ${e.message}"
                                showValidationError = true
                                isSubmitting = false
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp), // Reduced padding
                enabled = !isSubmitting && isFormComplete,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormComplete) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                ),
                contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp) // Reduced padding
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Submitting Report...",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Submit Water Quality Report",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            // Form Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isFormComplete) 
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    else 
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(6.dp) // Reduced corner radius
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), // Reduced padding
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isFormComplete) Icons.Filled.CheckCircle else Icons.Filled.Info,
                        contentDescription = "Form Status",
                        tint = if (isFormComplete) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp) // Reduced icon size
                    )
                    Spacer(modifier = Modifier.width(6.dp)) // Reduced spacing
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isFormComplete) "Form Complete" else "Form Incomplete",
                            style = MaterialTheme.typography.bodySmall, // Reduced from bodyMedium
                            fontWeight = FontWeight.Medium,
                            color = if (isFormComplete) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = if (isFormComplete) 
                                "Ready to submit${if (uploadedPhotos.isNotEmpty()) " with ${uploadedPhotos.size} photo(s)" else ""}"
                            else 
                                "Please fill in all required fields",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            // Add bottom padding to ensure content doesn't go behind navigation bar
            Spacer(modifier = Modifier.height(80.dp)) // Reduced from 120dp
        }
    }

    // Photo Picker Dialog
    if (showPhotoPicker) {
        PhotoPickerDialog(
            onDismiss = { showPhotoPicker = false },
            onPhotoSelected = { photoName, photoSize ->
                val newPhoto = PhotoItem(
                    id = UUID.randomUUID().toString(),
                    name = photoName,
                    size = photoSize
                )
                uploadedPhotos.add(newPhoto)
                showPhotoPicker = false
            }
        )
    }
    
    // Validation Error Dialog
    if (showValidationError) {
        AlertDialog(
            onDismissRequest = { showValidationError = false },
            title = {
                Text(
                    text = "Validation Error",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            },
            text = {
                Text(
                    text = validationMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            confirmButton = {
                Button(
                    onClick = { showValidationError = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("OK")
                }
            }
        )
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
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp), // Reduced padding
            verticalArrangement = Arrangement.spacedBy(8.dp) // Reduced spacing
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium, // Reduced from titleLarge
                fontWeight = FontWeight.Medium, // Reduced font weight
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall, // Reduced from bodyMedium
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                lineHeight = 14.sp // Reduced line height
            )

            Spacer(modifier = Modifier.height(4.dp)) // Reduced spacer
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
private fun PhotoUploadSection(
    uploadedPhotos: List<PhotoItem>,
    onAddPhoto: () -> Unit,
    onRemovePhoto: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Upload Button Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
            ),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp) // Reduced from 20dp
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp) // Reduced from 12dp
            ) {
                Icon(
                    imageVector = Icons.Outlined.FileUpload,
                    contentDescription = "Upload Photos",
                    modifier = Modifier.size(40.dp), // Reduced from 56dp
                    tint = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                )

                Text(
                    text = "Upload Photos",
                    style = MaterialTheme.typography.bodyMedium, // Reduced from titleSmall
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Text(
                    text = "JPG, PNG. Max 10MB per photo.", // Shortened text
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )

                Button(
                    onClick = onAddPhoto,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(6.dp), // Reduced corner radius
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp) // Reduced padding
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Photo",
                        modifier = Modifier.size(16.dp) // Reduced from 20dp
                    )
                    Spacer(modifier = Modifier.width(6.dp)) // Reduced spacing
                    Text("Add Photos")
                }

                // Photo Suggestions
                Text(
                    text = "Suggestions:", // Shortened text
                    style = MaterialTheme.typography.bodySmall, // Reduced from labelLarge
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                val suggestions = listOf(
                    "Water color",
                    "Visible particles",
                    "Water source",
                    "Surrounding area"
                ) // Shortened suggestions

                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    suggestions.forEach { suggestion ->
                        Row(
                            modifier = Modifier.padding(vertical = 2.dp), // Reduced from 4dp
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.PhotoCamera,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp), // Reduced from 18dp
                                tint = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.width(6.dp)) // Reduced from 8dp
                            Text(
                                text = suggestion,
                                style = MaterialTheme.typography.bodySmall, // Reduced from bodyMedium
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }
        }

        // Uploaded Photos Display
        if (uploadedPhotos.isNotEmpty()) {
            Text(
                text = "Photos (${uploadedPhotos.size})", // Shortened text
                style = MaterialTheme.typography.bodyMedium, // Reduced from titleMedium
                fontWeight = FontWeight.Medium, // Reduced from Bold
                color = MaterialTheme.colorScheme.onSurface
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp), // Reduced from 12dp
                contentPadding = PaddingValues(vertical = 4.dp) // Reduced from 8dp
            ) {
                items(uploadedPhotos) { photo ->
                    PhotoCard(
                        photo = photo,
                        onRemove = { onRemovePhoto(photo.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PhotoCard(
    photo: PhotoItem,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.size(100.dp), // Reduced from 120dp
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(6.dp), // Reduced from 8dp
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Photo placeholder (in real app, this would show actual image)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp), // Reduced from 8dp
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Photo,
                    contentDescription = "Photo",
                    modifier = Modifier.size(32.dp), // Reduced from 40dp
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(2.dp)) // Reduced from 4dp
                Text(
                    text = photo.name,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    maxLines = 1, // Reduced from 2
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = photo.size,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            // Remove button
            IconButton(
                onClick = onRemove,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(20.dp) // Reduced from 24dp
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Remove Photo",
                    modifier = Modifier.size(14.dp), // Reduced from 16dp
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun PhotoPickerDialog(
    onDismiss: () -> Unit,
    onPhotoSelected: (String, String) -> Unit
) {
    val samplePhotos = listOf(
        "water_source.jpg" to "2.3 MB",
        "water_color.jpg" to "1.8 MB",
        "particles.jpg" to "3.1 MB",
        "tap_photo.jpg" to "2.7 MB",
        "well_area.jpg" to "4.2 MB"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Select Photos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Choose photos to upload:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                samplePhotos.forEach { (photoName, photoSize) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Photo,
                            contentDescription = "Photo",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = photoName,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = photoSize,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        Button(
                            onClick = { onPhotoSelected(photoName, photoSize) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("Select")
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
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
