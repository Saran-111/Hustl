import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from firebase_admin import messaging
import threading
import time
import os

# To run this script:
# 1. Download your Service Account Key from Firebase Console -> Project Settings -> Service Accounts -> Generate new private key.
# 2. Rename the downloaded JSON file to 'serviceAccountKey.json' and place it in this folder.
# 3. Run: python server.py

KEY_PATH = "serviceAccountKey.json"

if not os.path.exists(KEY_PATH):
    print("=========================================================")
    print("ERROR: serviceAccountKey.json not found!")
    print("Please download your Service Account Key from Firebase Console:")
    print("Settings (Gear icon) -> Project settings -> Service accounts -> Generate new private key")
    print("Save it in this 'backend' folder as 'serviceAccountKey.json' and restart the script.")
    print("=========================================================")
    exit(1)

# Initialize Firebase Admin
cred = credentials.Certificate(KEY_PATH)
firebase_admin.initialize_app(cred)

db = firestore.client()

print("🚀 Hustl Push Notification Server is running...")
print("Listening for new notifications to push via FCM...\n")

# Use an event to keep the main thread alive
callback_done = threading.Event()

def on_snapshot(col_snapshot, changes, read_time):
    for change in changes:
        if change.type.name == 'ADDED':
            doc = change.document.to_dict()
            doc_id = change.document.id
            
            # Check if this notification has already been pushed to FCM
            if doc.get("isPushed", False):
                continue

            target_user_id = doc.get("targetUserId")
            title = doc.get("title", "Hustl Notification")
            body = doc.get("body", "")
            notif_type = doc.get("type", "general")

            if not target_user_id:
                continue

            # Fetch the user's FCM token
            user_ref = db.collection("users").document(target_user_id).get()
            if not user_ref.exists:
                continue
            
            user_data = user_ref.to_dict()
            fcm_token = user_data.get("fcmToken")

            if not fcm_token:
                print(f"⚠️ User {target_user_id} has no FCM token. Skipping push.")
                # Mark as pushed anyway so we don't keep trying
                db.collection("notifications").document(doc_id).update({"isPushed": True})
                continue

            # Send the FCM message
            message = messaging.Message(
                notification=messaging.Notification(
                    title=title,
                    body=body
                ),
                data={
                    "type": notif_type
                },
                token=fcm_token
            )

            try:
                response = messaging.send(message)
                print(f"✅ Successfully sent push notification to {target_user_id} (Msg ID: {response})")
                # Mark as pushed
                db.collection("notifications").document(doc_id).update({"isPushed": True})
            except Exception as e:
                print(f"❌ Failed to send push to {target_user_id}: {e}")

# Watch the notifications collection
col_query = db.collection("notifications").where("isPushed", "==", False)

# Watch the collection
query_watch = col_query.on_snapshot(on_snapshot)

try:
    while True:
        time.sleep(1)
except KeyboardInterrupt:
    print("\nShutting down server...")
